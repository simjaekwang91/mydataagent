package com.openai.mydataagent.adapter.`in`.restapi.mapper

import com.openai.mydataagent.adapter.`in`.restapi.dto.ConversationHistoryResponseDto
import com.openai.mydataagent.adapter.`in`.restapi.dto.ConversationResponseDto
import com.openai.mydataagent.domain.ConversationDto
import com.openai.mydataagent.domain.ConversationHistoryDto

object ConversationHistoryMapper {
    fun toDto(conversationHistoryDto: ConversationHistoryResponseDto): ConversationHistoryDto {
        return ConversationHistoryDto(
            id = conversationHistoryDto.id,
            userId = conversationHistoryDto.userId,
            roomId = conversationHistoryDto.roomId,
            conversationList = conversationHistoryDto.conversationList.map {
                toConversationDto(it)
            }.toMutableList()
        )
    }

    fun fromDto(dto: ConversationHistoryDto): ConversationHistoryResponseDto {
        return ConversationHistoryResponseDto(
            id = dto.id,
            userId = dto.userId,
            roomId = dto.roomId,
            conversationList = dto.conversationList.map {
                toResponseDto(it)
            }.toMutableList()
        )
    }

    private fun toConversationDto(conversationResponseDto: ConversationResponseDto): ConversationDto {
        return ConversationDto(
            question = conversationResponseDto.question,
            response = conversationResponseDto.response,
            createTime = conversationResponseDto.createTime
        )
    }

    private fun toResponseDto(dto: ConversationDto): ConversationResponseDto {
        return ConversationResponseDto(
            question = dto.question,
            response = dto.response,
            createTime = dto.createTime
        )
    }
}
