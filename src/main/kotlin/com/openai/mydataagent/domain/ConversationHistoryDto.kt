package com.openai.mydataagent.domain

import java.time.Instant


data class ConversationHistoryDto (
    val id : String? = null,
    val userId: String,
    val roomId: String,
    val conversationList: MutableList<ConversationDto>
) {
    fun removeConversationHistory() {
        conversationList.removeFirst()
    }

    fun addConversationHistory(conversationDto: ConversationDto) {
        conversationList.add(conversationDto)
    }
}

data class ConversationDto (
    val question: String,
    val response: String,
    val createTime: Instant,
)

