package com.openai.mydataagent.application.service

import com.openai.mydataagent.application.port.`in`.QuestionCommand
import com.openai.mydataagent.application.port.`in`.QuestionUseCase
import com.openai.mydataagent.application.port.out.CacheConversactionPort
import com.openai.mydataagent.application.port.out.ChattingRoomListResponse
import com.openai.mydataagent.application.port.out.RagPort
import jakarta.annotation.PostConstruct
import java.awt.SystemColor.text
import java.io.File
import java.io.InputStream
import opennlp.tools.sentdetect.SentenceDetectorME
import opennlp.tools.sentdetect.SentenceModel
import org.springframework.stereotype.Service

/**
 * Question service
 *
 * @constructor Create empty Question service
 */
@Service
class QuestionService(private val cacheConversactionPort: CacheConversactionPort,
    private val ragPort: RagPort) : QuestionUseCase{

    init {
//        val text = File("src/main/resources/mydataguide.txt").readText(Charsets.UTF_8)
//        val sentences = text.split(Regex("(?<=\\.)|(?<=!)|(?<=\\?)"))
//
//        ragPort.savaRagDocument("Test", "content", sentences)
    }


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
