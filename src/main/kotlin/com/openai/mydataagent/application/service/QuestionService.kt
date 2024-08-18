package com.openai.mydataagent.application.service

import com.openai.mydataagent.application.port.`in`.QuestionUseCase
import com.openai.mydataagent.application.port.out.AIPort
import com.openai.mydataagent.application.port.out.CacheConversationPort
import com.openai.mydataagent.application.port.out.ConversationHistoryPort
import com.openai.mydataagent.application.port.out.RagPort
import com.openai.mydataagent.domain.ConversationDto
import com.openai.mydataagent.domain.ConversationHistoryDto
import com.openai.mydataagent.domain.QuestionDomainDto
import java.time.Instant
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran
import kr.co.shineware.nlp.komoran.model.KomoranResult
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Question service
 *
 * @constructor Create empty Question service
 */
@Service
class QuestionService(
    private val cacheConversationPort: CacheConversationPort,
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
            cacheConversationPort.getCacheData(cacheKey, ConversationHistoryDto::class.java)

        // 질문 준비
        val questions = prepareQuestions(questionDomainDto, cacheHistory, 8076)

        // open ai api 호출
        val aiMessage = aiPort.getAIResponse(questions, isFirstQuestion)

        // 대화 내용 캐시 및 DB 저장 멀티 스레드 호출
        setHistoryAsync(cacheKey, questionDomainDto, aiMessage)

        return aiMessage
    }

    fun prepareQuestions(questionDomainDto: QuestionDomainDto, cacheHistory: ConversationHistoryDto?, maxTokens: Int): MutableList<String> {
        val questions = mutableListOf<String>()

        // 이전 대화 내용을 추가 (토큰 수를 초과하지 않도록 제한)
        cacheHistory?.conversationList?.forEachIndexed { index, value ->
            questions.add(index, "[이전 대화 내용] 질문:${value.question} 답변:${value.response}")
        }

        ragPort.searchSimilarVectors(questionDomainDto.message).forEach {
            questions.add("[참고 데이터] $it")
        }

        // ai 질문 추가
        questions.add(
            "위 검색된 내용을 바탕으로 '${questionDomainDto.message}' 이 질문 내용에 가장 적합한 답을 찾아줘. " +
                    "검색된 내용은 [참고 데이터] prefix 붙어있고 [이전 대화 내용]은 너랑 이야기 했던 대화 내용이야" +
                    "마크 다운 문자열은 제거하고" +
                    "니가 판단할때 질문 내용이 검색을 뜻하는게 아니라면 자유롭게 답변해."
        )

        // 토큰 제한을 초과하지 않도록 제한
        while (calculateTokenCount(questions.joinToString(" ")) > maxTokens && questions.isNotEmpty()) {
            questions.removeAt(0)  // 가장 이전 대화 내역 먼저 삭제
        }

        return questions
    }

    fun calculateTokenCount(text: String): Int {
        return text.length / 3 // 한국어 이기 때문에 대략 3글자에 1 토큰으로 계산했음
    }

    /**
     * 대화내역 캐시(최근 10개) 및 전체 대화내역 저장
     *
     * @param key
     * @param questionDomainDto
     * @param responseMessage
     * @return
     */
    fun setHistoryAsync(key: String, questionDomainDto: QuestionDomainDto, responseMessage: String): CompletableFuture<Void> {
        val setCacheDataFuture = CompletableFuture.runAsync({
            setCacheData(key, questionDomainDto, responseMessage)
        }, executorService)

        val setTotalConversationFuture = CompletableFuture.runAsync({
            setTotalConversation(questionDomainDto, responseMessage)
        }, executorService)

        return CompletableFuture.allOf(setCacheDataFuture, setTotalConversationFuture)
    }

    /**
     * 최근 10개 대화 캐싱
     *
     * @param key
     * @param questionDomainDto
     * @param responseMessage
     */
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

    /**
     * 전체 대화 내역 저장
     *
     * @param questionDomainDto
     * @param responseMessage
     */
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

    /**
     * 캐싱 및 DB 저장이 중복 코드가 많아 공통화
     *
     * @param getHistory 캐시 및 DB에서 대화 내역 가져오는 함수
     * @param updateHistory 캐시 및 DB에 대화 내역 저장 함수
     * @param createNewHistory 신규 대화 내역
     * @param currentConversationDto 신규 대화 상세 내역
     * @param preUpdateAction 작업 전 수행할 함수
     * @receiver
     * @receiver
     * @receiver
     */
    fun handleConversationHistory(
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
