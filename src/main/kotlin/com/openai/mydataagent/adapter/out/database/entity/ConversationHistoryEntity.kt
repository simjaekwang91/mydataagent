package com.openai.mydataagent.adapter.out.database.entity

import java.time.Instant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "conversation")
data class ConversationHistoryEntity (
    @Id
    val id: String? = null,
    val userId: String,
    val roomId: String,
    val conversationList: List<ConversationEntity>
    )

data class ConversationEntity (
    val question: String,
    val response: String,
    val createTime: Instant
)
