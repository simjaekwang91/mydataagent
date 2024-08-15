package com.openai.mydataagent.application.port.out

import com.openai.mydataagent.domain.ConversationHistoryDto

interface ConversationHistoryPort {
    fun getConversationList(userId: String, roomId: String): List<ConversationHistoryDto>

    fun setConversationList(conversationHistoryDto: ConversationHistoryDto): Boolean
}
