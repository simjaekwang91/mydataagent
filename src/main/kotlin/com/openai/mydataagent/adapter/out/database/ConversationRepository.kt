package com.openai.mydataagent.adapter.out.database

import com.openai.mydataagent.adapter.out.database.entity.ConversationHistoryEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface ConversationRepository : MongoRepository<ConversationHistoryEntity, String> {
    fun findByUserIdAndRoomId(userId: String, roomId: String): List<ConversationHistoryEntity>
    fun findByUserId(userId: String): List<ConversationHistoryEntity>

}
