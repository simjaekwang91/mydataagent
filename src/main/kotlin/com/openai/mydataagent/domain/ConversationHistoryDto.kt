package com.openai.mydataagent.domain


data class ConversationHistoryDto (
    val userId: String,
    val roomId: String,
    val conversationList: MutableList<ConversationDto>
) {
    fun removeConversationHistory() {
        conversationList.removeFirst()
    }
}

data class ConversationDto (
    val question: String,
    val response: String,
)

