package com.openai.mydataagent.adapter.out.aiapi

import com.openai.mydataagent.application.port.out.AIPort
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.stereotype.Service

@Service
class OpenAIApiImpl(private val openAiChatModel: OpenAiChatModel): AIPort {
    override fun getAIResponse(query: List<String>, isFistMessage: Boolean): String {
        return try {
            val promptMessage = mutableListOf<Message>()
            if(isFistMessage) {
                promptMessage.add(SystemMessage("너는 금융권의 질의 응답자야"))
            }

            query.forEach {
                promptMessage.add(UserMessage(it))
            }

            val messageList = org.springframework.ai.chat.prompt.Prompt(
                promptMessage
            )

            openAiChatModel.call(messageList).result.output.content
        } catch (e: Exception){
            throw e
        }
    }
}
