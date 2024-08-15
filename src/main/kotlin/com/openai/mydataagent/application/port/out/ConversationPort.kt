package com.openai.mydataagent.application.port.out

import com.openai.mydataagent.adapter.out.database.entity.ConversationHistoryEntity

interface ConversationPort {
    fun getConversationList(userId: String, roomId: String): List<ConversationHistoryEntity>

    fun setConversationList(conversationList: ConversationHistoryEntity): Boolean
}
