package com.openai.mydataagent.application.port.out

import com.openai.mydataagent.domain.ConversationHistoryDto

interface ConversationHistoryPort {

    fun getAllConversationList(userId: String): List<ConversationHistoryDto>?
    fun getConversationList(userId: String, roomId: String): ConversationHistoryDto?
    fun setConversationList(conversationHistoryDto: ConversationHistoryDto): Boolean
}
