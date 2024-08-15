package com.openai.mydataagent.adapter.out.database.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "conversation")
data class ConversationHistoryEntity (
    @Id
    val id: String? = null,
    val userId: String,
    val roomId: String,
    val conversationList: List<Conversation>

    )

data class Conversation (
    val question: String,
    val response: String,
)
