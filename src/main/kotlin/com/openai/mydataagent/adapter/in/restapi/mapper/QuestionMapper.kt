package com.openai.mydataagent.adapter.`in`.restapi.mapper

import com.openai.mydataagent.adapter.`in`.restapi.dto.QuestionRequestDto
import com.openai.mydataagent.domain.QuestionDomainDto

object QuestionMapper {
    fun toCommand(requestDto: QuestionRequestDto): QuestionDomainDto {
        return QuestionDomainDto(
            userId = requestDto.userId,
            roomId = requestDto.roomId,
            message = requestDto.message
        )
    }

    fun fromCommand(command: QuestionDomainDto): QuestionRequestDto {
        return QuestionRequestDto(
            userId = command.userId,
            roomId = command.roomId,
            message = command.message
        )
    }
}
