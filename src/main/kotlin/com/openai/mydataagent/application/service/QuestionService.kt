package com.openai.mydataagent.application.service

import com.openai.mydataagent.application.port.`in`.QuestionCommand
import com.openai.mydataagent.application.port.`in`.QuestionUseCase
import com.openai.mydataagent.application.port.out.CacheConversactionPort
import com.openai.mydataagent.application.port.out.ChattingRoomListResponse
import com.openai.mydataagent.application.port.out.RequestAIPort
import com.openai.mydataagent.application.port.out.SaveConversationPort
import org.springframework.stereotype.Service

/**
 * Question service
 *
 * @constructor Create empty Question service
 */
@Service
class QuestionService(private val cacheConversactionPort: CacheConversactionPort) : QuestionUseCase{
    override fun getChattingRoomList(userId: String): ChattingRoomListResponse {
        TODO("Not yet implemented")
    }

    override fun requestQuestion(questionCommand: QuestionCommand): String? {
        return try {
            cacheConversactionPort.setCacheData("test", "test")
            return cacheConversactionPort.getCacheData("test", String::class.java)
        } catch(e: Exception) {
            "fail"
        }
    }
}
