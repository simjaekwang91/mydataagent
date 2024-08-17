package com.openai.mydataagent.adapter.`in`.restapi.dto

import com.openai.mydataagent.domain.ConversationDto
import java.time.Instant

class ConversationHistoryResponseDto  (
    val id : String? = null,
    val userId: String,
    val roomId: String,
    val conversationList: MutableList<ConversationResponseDto>
)
data class ConversationResponseDto (
    val question: String,
    val response: String,
    val createTime: Instant,
)
