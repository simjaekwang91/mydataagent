package com.openai.mydataagent.adapter.out.database

import com.openai.mydataagent.adapter.out.database.entity.ConversationHistoryEntity
import com.openai.mydataagent.application.port.out.ConversationPort
import org.springframework.stereotype.Repository

@Repository
class MongoImpl(private val conversationRepository: ConversationRepository): ConversationPort {

    override fun getConversationList(userId: String, roomId: String): List<ConversationHistoryEntity> {
        return conversationRepository.findByUserIdAndRoomId(userId, roomId)
    }

    override fun setConversationList(
        conversationList: ConversationHistoryEntity
    ): Boolean {
        return !conversationRepository.save(conversationList).id.isNullOrEmpty()
    }
}
