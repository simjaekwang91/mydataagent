package com.openai.mydataagent.application.port.out

import org.springframework.ai.document.Document

interface RagPort {
    fun findRagByWord(query: String):List<Document>

    fun savaRagDocument(content: List<String>)

}
