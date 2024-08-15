package com.openai.mydataagent.application.service

import com.openai.mydataagent.application.port.`in`.QuestionUseCase
import com.openai.mydataagent.application.port.out.AIPort
import com.openai.mydataagent.application.port.out.CacheConversactionPort
import com.openai.mydataagent.application.port.out.ConversationHistoryPort
import com.openai.mydataagent.application.port.out.RagPort
import com.openai.mydataagent.domain.ChattingRoomListDomainDto
import com.openai.mydataagent.domain.ConversationDto
import com.openai.mydataagent.domain.ConversationHistoryDto
import com.openai.mydataagent.domain.QuestionDomainDto
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Question service
 *
 * @constructor Create empty Question service
 */
@Service
class QuestionService(
    private val cacheConversationPort: CacheConversactionPort,
    private val ragPort: RagPort,
    private val aiPort: AIPort,
    private val conversationPort: ConversationHistoryPort
) : QuestionUseCase {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private val executorService: ExecutorService = Executors.newCachedThreadPool()
    }

    override fun getChattingRoomList(userId: String): ChattingRoomListDomainDto {
        TODO("Not yet implemented")
    }

    override fun requestQuestion(questionDomainDto: QuestionDomainDto): String? {
        return try {
            if (questionDomainDto.roomId.isNotEmpty()) {
                handleQuestion(questionDomainDto, questionDomainDto.roomId)
            } else {
                handleQuestion(questionDomainDto, UUID.randomUUID().toString())
            }
        } catch (e: Exception) {
            logger.error("Error requestQuestion ", e)
            throw e
        }
    }

    private fun handleQuestion(questionDomainDto: QuestionDomainDto, roomId: String): String {
        val questions = mutableListOf<String>()
        //이전 대화 내용이 있을 경우?
        val cacheHistory = questionDomainDto.roomId.takeIf { it.isNotEmpty() }?.let {
            cacheConversationPort.getCacheData(
                "${questionDomainDto.userId}:$it",
                ConversationHistoryDto::class.java
            )
        }

        cacheHistory?.conversationList?.forEachIndexed{ index, value ->
            questions.add(index, "[이전 대화 내용] $value")
        }

        ragPort.findRagByWord(questionDomainDto.message).forEach {
            questions.add("[참고 데이터] $it")
        }

        //질문 내용 add
        questions.add("위 검색된 내용을 바탕으로 '${questionDomainDto.message}' 이 질문 내용에 가장 적합한 답을 찾아줘 검색된 내용은 [참고 데이터] prefix 붙어있어")

        //ai 질문
        val aiMessage = aiPort.getAIResponse(questions, true)

        //TODO: 최근 10개 내역만 cacheData 에 저장 필요
        val conversationHistoryDto = ConversationHistoryDto(
            userId = questionDomainDto.userId,
            roomId = roomId,
            conversationList = mutableListOf(
                ConversationDto(questionDomainDto.message, aiMessage)
            )
        )

        cacheHistory?.let { conversationHistoryDto.conversationList.addAll(0, it.conversationList) }

        setHistoryAsync("${questionDomainDto.userId}:${roomId}", conversationHistoryDto)

        return aiMessage
    }

    fun setHistoryAsync(key: String, data: ConversationHistoryDto) {
        executorService.submit {
            cacheConversationPort.setCacheData(
                key,
                data
            )
            conversationPort.setConversationList(data)
        }.get()
    }
}
