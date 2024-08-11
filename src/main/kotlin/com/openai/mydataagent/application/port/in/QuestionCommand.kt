package com.openai.mydataagent.application.port.`in`

data class QuestionCommand (
    val roomId : String,
    val message : String,
)
