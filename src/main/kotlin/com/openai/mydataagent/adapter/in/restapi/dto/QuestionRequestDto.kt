package com.openai.mydataagent.adapter.`in`.restapi.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "질문하기 요청 모델")
data class QuestionRequestDto(
    val userId : String,
    val roomId : String? = null,
    val message : String,
)
