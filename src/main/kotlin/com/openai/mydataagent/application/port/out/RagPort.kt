package com.openai.mydataagent.application.port.out

interface RagPort {
    fun searchSimilarVectors(query: String):List<String>

    fun savaRagDocument(content: List<String>)

}
