package com.openai.mydataagent.adapter.out.database

import com.openai.mydataagent.adapter.out.database.entity.ConversationHistoryEntity
import com.openai.mydataagent.adapter.out.database.mapper.ConversationHistoryMapper
import com.openai.mydataagent.application.port.out.ConversationHistoryPort
import com.openai.mydataagent.domain.ConversationHistoryDto
import org.springframework.stereotype.Repository

@Repository
class MongoImpl(private val conversationRepository: ConversationRepository): ConversationHistoryPort {
    override fun getAllConversationList(userId: String): List<ConversationHistoryDto>? {
        return conversationRepository.findByUserId(userId).takeIf { it.isNotEmpty() }?.map {
            ConversationHistoryMapper.toDto(it)
        }
    }

    override fun getConversationList(userId: String, roomId: String): ConversationHistoryDto? {
        return conversationRepository.findByUserIdAndRoomId(userId, roomId).firstOrNull()?.let {
            ConversationHistoryMapper.toDto(it)
        }
    }

    override fun setConversationList(
        conversationHistoryDto: ConversationHistoryDto
    ): Boolean {
        return !conversationRepository.save(ConversationHistoryMapper.fromDto(conversationHistoryDto)).id.isNullOrEmpty()
    }
}
