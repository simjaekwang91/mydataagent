package com.openai.mydataagent.application.port.`in`

import com.openai.mydataagent.domain.ConversationHistoryDto
import com.openai.mydataagent.domain.QuestionDomainDto

interface QuestionUseCase {
    fun getAllConversationList(userId: String) : List<ConversationHistoryDto>?
    fun requestQuestion(questionDomainDto: QuestionDomainDto) : String?
}
