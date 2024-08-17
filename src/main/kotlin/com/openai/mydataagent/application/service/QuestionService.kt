package com.openai.mydataagent.application.service

import com.openai.mydataagent.application.port.`in`.QuestionUseCase
import com.openai.mydataagent.application.port.out.AIPort
import com.openai.mydataagent.application.port.out.CacheConversactionPort
import com.openai.mydataagent.application.port.out.ConversationHistoryPort
import com.openai.mydataagent.application.port.out.RagPort
import com.openai.mydataagent.domain.ConversationDto
import com.openai.mydataagent.domain.ConversationHistoryDto
import com.openai.mydataagent.domain.QuestionDomainDto
import java.time.Instant
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

    override fun getAllConversationList(userId: String): List<ConversationHistoryDto>? {
        return conversationPort.getAllConversationList(userId)
    }

    override fun requestQuestion(questionDomainDto: QuestionDomainDto): String? {
        return try {
            handleQuestion(questionDomainDto)
        } catch (e: Exception) {
            logger.error("Error requestQuestion ", e)
            throw e
        }
    }

    private fun handleQuestion(questionDomainDto: QuestionDomainDto): String {
        // 캐시된 대화 이력을 가져옴
        val isFirstQuestion = questionDomainDto.roomId.isEmpty()
        if(isFirstQuestion) {
            questionDomainDto.setRoomId()
        }

        val cacheKey = "${questionDomainDto.userId}:${questionDomainDto.roomId}"

        val cacheHistory = if (isFirstQuestion) null else
            cacheConversationPort.getCacheData("", ConversationHistoryDto::class.java)

        // 질문 준비
        val questions = prepareQuestions(questionDomainDto, cacheHistory, 8076)

        // AI로 질문을 전송하고 응답을 받음
        val aiMessage = aiPort.getAIResponse(questions, isFirstQuestion)

        // 대화 기록 저장 (비동기)
        setHistoryAsync(cacheKey, questionDomainDto, aiMessage)

        return aiMessage
    }

    fun prepareQuestions(questionDomainDto: QuestionDomainDto, cacheHistory: ConversationHistoryDto?, maxTokens: Int): MutableList<String> {
        val questions = mutableListOf<String>()

        // 이전 대화 내용을 추가 (토큰 수를 초과하지 않도록 제한)
        cacheHistory?.conversationList?.forEachIndexed { index, value ->
            questions.add(index, "[이전 대화 내용] ${value.question}")
        }

        // RAG 검색 결과 추가
        ragPort.searchSimilarVectors(questionDomainDto.message).forEach {
            questions.add("[참고 데이터] $it")
        }

        // 질문 내용 추가
        questions.add(
            "위 검색된 내용을 바탕으로 '${questionDomainDto.message}' 이 질문 내용에 가장 적합한 답을 찾아줘. " +
                    "검색된 내용은 [참고 데이터] prefix 붙어있어. " +
                    "마크 다운 문자열은 제거하고 줘. " +
                    "니가 판단할때 질문 내용이 검색을 뜻하는게 아니라면 자유롭게 답변해."
        )

        // 토큰 제한을 초과하지 않도록 잘라냄
        while (calculateTokenCount(questions.joinToString(" ")) > maxTokens && questions.isNotEmpty()) {
            questions.removeAt(0)  // 가장 앞쪽부터 삭제
        }

        return questions
    }

    fun calculateTokenCount(text: String): Int {
        // 텍스트에서 공백을 포함한 전체 길이를 기준으로 대략적인 토큰 수를 계산합니다.
        return text.length / 3 // 한국어에서 대략 3글자에 1 토큰으로 계산
    }

    fun setHistoryAsync(key: String, questionDomainDto: QuestionDomainDto, responseMessage: String) {
        executorService.submit {
            setCacheData(key, questionDomainDto, responseMessage)
            setTotalConversation(questionDomainDto, responseMessage)
        }.get()
    }

    fun setCacheData(key: String, questionDomainDto: QuestionDomainDto, responseMessage: String) {
        val currentConversationDto = ConversationDto(questionDomainDto.message, responseMessage, Instant.now())

        handleConversationHistory(
            getHistory = { cacheConversationPort.getCacheData(key, ConversationHistoryDto::class.java) },
            updateHistory = { updatedHistory -> cacheConversationPort.setCacheData(key, updatedHistory) },
            createNewHistory = {
                ConversationHistoryDto(
                    userId = questionDomainDto.userId,
                    roomId = questionDomainDto.roomId,
                    conversationList = mutableListOf(currentConversationDto)
                )
            },
            currentConversationDto = currentConversationDto,
            preUpdateAction = { history ->
                while (history.conversationList.size >= 10) {
                    history.removeConversationHistory()
                }
            }
        )
    }

    fun setTotalConversation(questionDomainDto: QuestionDomainDto, responseMessage: String) {
        val currentConversationDto = ConversationDto(questionDomainDto.message, responseMessage, Instant.now())

        handleConversationHistory(
            getHistory = { conversationPort.getConversationList(questionDomainDto.userId, questionDomainDto.roomId) },
            updateHistory = { updatedHistory -> conversationPort.setConversationList(updatedHistory) },
            createNewHistory = {
                ConversationHistoryDto(
                    userId = questionDomainDto.userId,
                    roomId = questionDomainDto.roomId,
                    conversationList = mutableListOf(currentConversationDto)
                )
            },
            currentConversationDto = currentConversationDto
        )
    }

    private fun handleConversationHistory(
        getHistory: () -> ConversationHistoryDto?,
        updateHistory: (ConversationHistoryDto) -> Unit,
        createNewHistory: () -> ConversationHistoryDto,
        currentConversationDto: ConversationDto,
        preUpdateAction: ((ConversationHistoryDto) -> Unit)? = null
    ) {
        getHistory()?.takeIf { it.conversationList.isNotEmpty() }?.let { history ->
            preUpdateAction?.invoke(history)
            history.addConversationHistory(currentConversationDto)
            updateHistory(history)
        } ?: run {
            updateHistory(createNewHistory())
        }
    }
}
