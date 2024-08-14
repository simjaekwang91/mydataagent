package com.openai.mydataagent.application.port.out

interface RagPort {
    fun findRagByWord()

    fun savaRagDocument(className: String, contentName: String, sentences: List<String>)

}
