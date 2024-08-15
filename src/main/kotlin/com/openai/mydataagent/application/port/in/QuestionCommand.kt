package com.openai.mydataagent.application.port.`in`

data class QuestionCommand (
    val userId : String,
    val roomId : String,
    val message : String,
)
