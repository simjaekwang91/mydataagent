package com.openai.mydataagent.application.port.out

interface AIPort {
    fun getAIResponse(query: String): String
}
