package com.openai.mydataagent.adapter.`in`.restapi.mapper

import com.openai.mydataagent.adapter.`in`.restapi.dto.QuestionRequestDto
import com.openai.mydataagent.application.port.`in`.QuestionCommand

object QuestionMapper {
    fun toCommand(requestDto: QuestionRequestDto): QuestionCommand {
        return QuestionCommand(
            userId = requestDto.userId,
            roomId = requestDto.roomId,
            message = requestDto.message
        )
    }

    fun fromCommand(command: QuestionCommand): QuestionRequestDto {
        return QuestionRequestDto(
            userId = command.userId,
            roomId = command.roomId,
            message = command.message
        )
    }
}
