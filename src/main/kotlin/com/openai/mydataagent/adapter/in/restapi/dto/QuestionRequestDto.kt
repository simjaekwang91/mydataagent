package com.openai.mydataagent.adapter.`in`.restapi.dto

data class QuestionRequestDto(
    val userId : String,
    val roomId : String,
    val message : String,
)
