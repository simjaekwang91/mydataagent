package com.openai.mydataagent.application

import com.openai.mydataagent.application.port.out.AIPort
import com.openai.mydataagent.application.port.out.CacheConversationPort
import com.openai.mydataagent.application.port.out.ConversationHistoryPort
import com.openai.mydataagent.application.port.out.RagPort
import com.openai.mydataagent.application.service.QuestionService
import com.openai.mydataagent.domain.ConversationDto
import com.openai.mydataagent.domain.ConversationHistoryDto
import com.openai.mydataagent.domain.QuestionDomainDto
import java.time.Instant
import java.util.*
import java.util.stream.Stream
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)

class QuestionServiceTest {
    companion object {
        @JvmStatic
        fun provideMaxTokenValues(): Stream<Int> {
            return Stream.of(1000, 74)
        }
        @JvmStatic
        fun getRandomInt(
            from: Int,
            to: Int,
        ): Int {
            return Random().nextInt(to - from) + from
        }
    }

    private lateinit var questionService: QuestionService

    @Mock
    private lateinit var cacheConversationPort: CacheConversationPort

    @Mock
    private lateinit var ragPort: RagPort

    @Mock
    private lateinit var aiPort: AIPort

    @Mock
    private lateinit var conversationHistoryPort: ConversationHistoryPort

    private val questionDomainDto = QuestionDomainDto("user1", "room1", "마이데이터")
    private val history = ConversationHistoryDto(
        userId = "user1",
        roomId = "room1",
        conversationList = mutableListOf(
            ConversationDto("question", "answer", Instant.now()),
            ConversationDto("question2", "answer2", Instant.now()),
            ConversationDto("question3", "answer3", Instant.now())
        )
    )

    private val cacheKey = "user1:room1"

    @BeforeEach
    fun setUp() {
        `when`(conversationHistoryPort.getConversationList(questionDomainDto.userId, questionDomainDto.roomId))
            .thenReturn(history)

        `when`(cacheConversationPort.getCacheData(cacheKey, ConversationHistoryDto::class.java)).thenReturn(
            history
        )
        `when`(ragPort.searchSimilarVectors("마이데이터")).thenReturn(
            listOf("Test1", "Test2")
        )
        `when`(aiPort.getAIResponse(listOf("test"), true)).thenReturn(
            "Test 입니다."
        )
        questionService = QuestionService(
            cacheConversationPort, ragPort, aiPort, conversationHistoryPort
        )
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    fun prepareQuestionsTest() {
        val result = questionService.prepareQuestions(questionDomainDto, history, 1000)

        assertNotNull(result)
        assertTrue(result.any { it.contains("마이데이터") })
        assertTrue(result.any { it.contains("[참고 데이터] Test1") })
        assertTrue(result.any { it.contains("[이전 대화 내용] 질문:question2 답변:answer2") })
    }

    @ParameterizedTest
    @MockitoSettings(strictness = Strictness.LENIENT)
    @MethodSource("provideMaxTokenValues")
    fun tokenLimitTest(maxTokens: Int) {
        val result = questionService.prepareQuestions(questionDomainDto, history, maxTokens)

        assertNotNull(result)
        if (maxTokens >= 1000) {
            assertTrue(result.any { it.contains("마이데이터") })
            assertTrue(result.any { it.contains("[참고 데이터] Test1") })
            assertTrue(result.any { it.contains("[이전 대화 내용] 질문:question 답변:answer") })
            assertTrue(result.any { it.contains("[이전 대화 내용] 질문:question2 답변:answer2") })
            assertTrue(result.any { it.contains("[이전 대화 내용] 질문:question3 답변:answer3") })
        } else if (maxTokens < 75) {
            //토큰 제한에 걸려 이전 대화 내용을 삭제
            assertTrue(result.none { it.contains("[이전 대화 내용] 질문:question 답변:answer") })
            assertTrue(result.none { it.contains("[이전 대화 내용] 질문:question2 답변:answer2") })
            assertTrue(result.any { it.contains("[참고 데이터] Test1") })
            assertTrue(result.any { it.contains("[이전 대화 내용] 질문:question3 답변:answer3") })
        }
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    fun setCacheDataTest() {
        //given
        val responseMessage = "Test response"

        // when
        questionService.setCacheData(cacheKey, questionDomainDto, responseMessage)

        // then
        val cachedHistory = cacheConversationPort.getCacheData(cacheKey, ConversationHistoryDto::class.java)

        assertNotNull(cachedHistory)
        assertEquals(4, cachedHistory.conversationList.size) // 기존 3개 대화 + 새로운 1개 대화
        assertEquals("Test response", cachedHistory.conversationList.last().response)
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    fun setTotalConversationTest() {
        // Given
        val responseMessage = "Test response"

        // When
        questionService.setTotalConversation(questionDomainDto, responseMessage)

        // then
        val updatedHistory = conversationHistoryPort.getConversationList(questionDomainDto.userId, questionDomainDto.roomId)

        assertNotNull(updatedHistory)
        assertEquals(4, updatedHistory.conversationList.size) // 기존 3개 대화 + 새로운 1개 대화
        assertEquals("Test response", updatedHistory.conversationList.last().response)
    }


}
