package com.openai.mydataagent.adapter.`in`.restapi.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "전체 대화내역 응답 모델")
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
