package com.openai.mydataagent.application.port.`in`

import com.openai.mydataagent.application.port.`in`.QuestionCommand
import com.openai.mydataagent.application.port.out.ChattingRoomListResponseCommand

interface QuestionUseCase {
    fun getChattingRoomList(userId: String) : ChattingRoomListResponseCommand
    fun requestQuestion(questionCommand: QuestionCommand) : String?
}
