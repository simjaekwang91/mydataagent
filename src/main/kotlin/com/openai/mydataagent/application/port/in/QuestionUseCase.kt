package com.openai.mydataagent.application.port.`in`

import com.openai.mydataagent.domain.ChattingRoomListDomainDto
import com.openai.mydataagent.domain.QuestionDomainDto

interface QuestionUseCase {
    fun getChattingRoomList(userId: String) : ChattingRoomListDomainDto
    fun requestQuestion(questionDomainDto: QuestionDomainDto) : String?
}
