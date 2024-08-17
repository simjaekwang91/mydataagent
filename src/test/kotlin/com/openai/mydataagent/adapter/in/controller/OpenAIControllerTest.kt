package com.openai.mydataagent.adapter.`in`.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.openai.mydataagent.adapter.`in`.restapi.OpenAIController
import com.openai.mydataagent.adapter.`in`.restapi.dto.ChattingRoomListResponseDto
import com.openai.mydataagent.adapter.`in`.restapi.model.ErrorCodeEnum
import com.openai.mydataagent.adapter.`in`.restapi.model.OpenAIResponse
import com.openai.mydataagent.domain.QuestionDomainDto
import com.openai.mydataagent.application.port.`in`.QuestionUseCase
import com.openai.mydataagent.domain.ChattingRoom
import com.openai.mydataagent.domain.ChattingRoomListDomainDto
import java.time.Instant
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(OpenAIController::class)
class OpenAIControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var questionUseCase: QuestionUseCase


    @BeforeEach
    fun setUp() {
        objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
        `when`(questionUseCase.requestQuestion(QuestionDomainDto("user1","1", "hi"))).thenReturn(
            "hello"
        )
        `when`(questionUseCase.getAllConversationList("1")).thenReturn(
            ChattingRoomListDomainDto(mutableListOf(
                ChattingRoom("1","2", Instant.now(), Instant.now()),
                ChattingRoom("2","3", Instant.now(), Instant.now())
            ))
        )
    }

    @Test
    fun getChattingRoomSuccessTest() {
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/openai/getchatlist/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn()


        val contentString = result.response.contentAsString
        // JSON 응답을 객체로 변환 (OpenAIResponse<ChattingRoomListResponseDto> 타입으로 변환)
        val apiResponse = objectMapper.readValue(
            contentString,
            object : com.fasterxml.jackson.core.type.TypeReference<OpenAIResponse<ChattingRoomListResponseDto>>() {}
        )

        // 데이터 필드에 대한 검증
        Assertions.assertNotNull(apiResponse.data, "Data should not be null")
        Assertions.assertEquals(ErrorCodeEnum.Success, apiResponse.errorCode)
        Assertions.assertEquals(2, apiResponse.data.chattingRoomList.size)
        Assertions.assertEquals("1", apiResponse.data.chattingRoomList[0].roomId)
        Assertions.assertEquals("2", apiResponse.data.chattingRoomList[0].title)
    }
}
