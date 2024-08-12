package com.openai.mydataagent.application.port.`in`

import com.openai.mydataagent.application.port.out.ChattingRoomListResponse

interface QuestionUseCase {
    fun getChattingRoomList(userId: String) : ChattingRoomListResponse
    fun requestQuestion(questionCommand: QuestionCommand) : String?
}
