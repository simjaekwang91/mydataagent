package com.openai.mydataagent.adapter.`in`.restapi

import com.openai.mydataagent.adapter.`in`.restapi.response.ErrorCodeEnum
import com.openai.mydataagent.adapter.`in`.restapi.response.OpenAIResponse
import com.openai.mydataagent.application.port.`in`.QuestionCommand
import com.openai.mydataagent.application.port.`in`.QuestionUseCase
import com.openai.mydataagent.application.port.out.ChattingRoomListResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "OpenAI", description = "Open-AI Controller")
@RequestMapping("/v1/openai")
class OpenAIController(private val questionUseCase: QuestionUseCase) {

    @Operation(summary = "채팅방 리스트")
    @GetMapping("getchatlist/{userid}")
    fun getChattingRoomList(@PathVariable("userid") userId: String,): OpenAIResponse<ChattingRoomListResponse?> {
        return try {
            OpenAIResponse(ErrorCodeEnum.Success, questionUseCase.getChattingRoomList(userId))
        } catch (e: Exception) {
            OpenAIResponse(ErrorCodeEnum.InternalError, null)
        }
    }

    @Operation(summary = "질의응답")
    @PostMapping("request-question")
    fun requestQuestion(@RequestBody request: QuestionCommand): OpenAIResponse<String> {
        return OpenAIResponse(ErrorCodeEnum.Success, "response")
    }

}
