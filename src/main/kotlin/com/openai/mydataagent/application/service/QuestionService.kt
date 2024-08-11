package com.openai.mydataagent.application.service

import com.openai.mydataagent.application.port.`in`.QuestionCommand
import com.openai.mydataagent.application.port.`in`.QuestionUseCase
import com.openai.mydataagent.application.port.out.ChattingRoomListResponse

class QuestionService : QuestionUseCase {
    override fun getChattingRoomList(userId: String): ChattingRoomListResponse {
        TODO("Not yet implemented")
    }

    override fun requestQuestion(questionCommand: QuestionCommand): String {
        TODO("Not yet implemented")
    }

}
