package com.openai.mydataagent.adapter.`in`.restapi

import com.openai.mydataagent.adapter.`in`.restapi.dto.ConversationHistoryResponseDto
import com.openai.mydataagent.adapter.`in`.restapi.dto.QuestionRequestDto
import com.openai.mydataagent.adapter.`in`.restapi.mapper.ConversationHistoryMapper
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

    @Operation(summary = "전체 대화 내역")
    @GetMapping("conversation/all/{userid}")
    fun getChattingRoomList(@PathVariable("userid") userId: String,): OpenAIResponse<List<ConversationHistoryResponseDto>?> {
        return try {
            val list = questionUseCase.getAllConversationList(userId)?.map {
                ConversationHistoryMapper.fromDto(it)
            }
            OpenAIResponse(ErrorCodeEnum.Success, list)
        } catch (e: Exception) {
            OpenAIResponse(ErrorCodeEnum.InternalError, null)
        }
    }

    @Operation(summary = "마이데이터 에이전트 질의 응답")
    @PostMapping("request-question")
    fun requestQuestion(@RequestBody request: QuestionRequestDto): OpenAIResponse<String?> {

        return OpenAIResponse(ErrorCodeEnum.Success, questionUseCase.requestQuestion(QuestionMapper.toCommand(request)))
    }
}
