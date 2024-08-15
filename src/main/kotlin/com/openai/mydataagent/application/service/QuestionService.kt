package com.openai.mydataagent.application.service

import com.openai.mydataagent.application.port.`in`.QuestionCommand
import com.openai.mydataagent.application.port.`in`.QuestionUseCase
import com.openai.mydataagent.application.port.out.AIPort
import com.openai.mydataagent.application.port.out.CacheConversactionPort
import com.openai.mydataagent.application.port.out.ChattingRoomListResponseCommand
import com.openai.mydataagent.application.port.out.RagPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Question service
 *
 * @constructor Create empty Question service
 */
@Service
class QuestionService(private val cacheConversactionPort: CacheConversactionPort,
    private val ragPort: RagPort, private val aiPort: AIPort ) : QuestionUseCase{

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }

    /**
     * 초기 PDF 를 통해 Vector DB 데이터 생성
     */
    init {
//        val text = File("src/main/resources/mydataguide.txt").readText(Charsets.UTF_8)
//        // ##를 기준으로 텍스트를 나누기
//        val sections = text.split(Regex("### |\\\\section\\*\\{[^}]*}")).filter { it.isNotBlank() }
//        val result = splitSectionsByTokenLimit(sections, 4000)
//        ragPort.savaRagDocument("Test", "content", result)
        val response = aiPort.getAIResponse("금융이 뭐야")
        logger.info(response)
    }

//    private final fun splitSectionsByTokenLimit(sections: List<String>, tokenLimit: Int): List<String> {
//        val result = mutableListOf<String>()
//
//        sections.forEach { section ->
//            if (section.length <= tokenLimit) {
//                result.add(section)
//            } else {
//                var startIndex = 0
//                while (startIndex < section.length) {
//                    val endIndex = minOf(startIndex + tokenLimit, section.length)
//                    result.add(section.substring(startIndex, endIndex))
//                    startIndex = endIndex
//                }
//            }
//        }
//
//        return result
//    }


    override fun getChattingRoomList(userId: String): ChattingRoomListResponseCommand {
        TODO("Not yet implemented")
    }

    override fun requestQuestion(questionCommand: QuestionCommand): String? {
        return try {
            cacheConversactionPort.setCacheData("test", "test")
            cacheConversactionPort.getCacheData("test", String::class.java)
        } catch(e: Exception) {
            logger.error(e.toString())
            throw e
        }
    }
}
