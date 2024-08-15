package com.openai.mydataagent.adapter.out.aiapi

import com.nimbusds.openid.connect.sdk.Prompt
import com.openai.mydataagent.application.port.out.AIPort
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.stereotype.Service

@Service
class OpenAIApiImpl(private val openAiChatModel: OpenAiChatModel): AIPort {
    override fun getAIResponse(query: String): String {

        val messageList = org.springframework.ai.chat.prompt.Prompt(
            listOf(
            SystemMessage("너는 금융권의 질의 응답자야"),
            UserMessage(query)
            )
        )

        return openAiChatModel.call(messageList).result.output.content
    }
}
