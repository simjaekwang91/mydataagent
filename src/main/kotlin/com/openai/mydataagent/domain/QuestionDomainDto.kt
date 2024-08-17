package com.openai.mydataagent.domain

import java.util.UUID

data class QuestionDomainDto (
    val userId : String,
    var roomId : String,
    val message : String,
) {
    fun setRoomId() {
        roomId = UUID.randomUUID().toString()
    }
}
