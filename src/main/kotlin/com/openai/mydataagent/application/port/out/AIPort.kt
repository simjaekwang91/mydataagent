package com.openai.mydataagent.application.port.out

interface AIPort {
    fun getAIResponse(query: List<String>, isFistMessage: Boolean): String
}
