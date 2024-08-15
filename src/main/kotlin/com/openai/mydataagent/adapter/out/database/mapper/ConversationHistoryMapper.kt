package com.openai.mydataagent.adapter.out.database.mapper

import com.openai.mydataagent.adapter.out.database.entity.ConversationEntity
import com.openai.mydataagent.adapter.out.database.entity.ConversationHistoryEntity
import com.openai.mydataagent.domain.ConversationDto
import com.openai.mydataagent.domain.ConversationHistoryDto

object ConversationHistoryMapper {
    fun toDto(conversationHistoryEntity: ConversationHistoryEntity): ConversationHistoryDto {
        return ConversationHistoryDto(
            conversationHistoryEntity.userId,
            conversationHistoryEntity.roomId,
            conversationHistoryEntity.conversationList.map {
                ConversationDto(
                    it.question,
                    it.response
                )
            }.toMutableList()
        )
    }

    fun fromDto(conversationHistoryDto: ConversationHistoryDto): ConversationHistoryEntity {
        return ConversationHistoryEntity(
            null,
            conversationHistoryDto.userId,
            conversationHistoryDto.roomId,
            conversationHistoryDto.conversationList.map {
                ConversationEntity(
                    it.question,
                    it.response
                )
            }
        )
    }
}
