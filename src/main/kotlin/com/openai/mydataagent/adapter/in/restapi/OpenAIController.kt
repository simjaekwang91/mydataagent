package com.openai.mydataagent.adapter.`in`.restapi

import com.openai.mydataagent.adapter.`in`.restapi.dto.ChattingRoomListResponseDto
import com.openai.mydataagent.adapter.`in`.restapi.dto.QuestionRequestDto
import com.openai.mydataagent.adapter.`in`.restapi.mapper.ChattingRoomListMapper
import com.openai.mydataagent.adapter.`in`.restapi.mapper.QuestionMapper
import com.openai.mydataagent.adapter.`in`.restapi.model.ErrorCodeEnum
import com.openai.mydataagent.adapter.`in`.restapi.model.OpenAIResponse
import com.openai.mydataagent.application.port.`in`.QuestionUseCase
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
    fun getChattingRoomList(@PathVariable("userid") userId: String,): OpenAIResponse<ChattingRoomListResponseDto?> {
        return try {
            val list = ChattingRoomListMapper.fromCommand(questionUseCase.getChattingRoomList(userId))
            OpenAIResponse(ErrorCodeEnum.Success, list)
        } catch (e: Exception) {
            OpenAIResponse(ErrorCodeEnum.InternalError, null)
        }
    }

    @Operation(summary = "질의응답")
    @PostMapping("request-question")
    fun requestQuestion(@RequestBody request: QuestionRequestDto): OpenAIResponse<String?> {

        return OpenAIResponse(ErrorCodeEnum.Success, questionUseCase.requestQuestion(QuestionMapper.toCommand(request)))
    }

}
