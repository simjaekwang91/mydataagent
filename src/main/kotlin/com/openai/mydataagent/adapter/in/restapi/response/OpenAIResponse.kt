package com.openai.mydataagent.adapter.`in`.restapi.response

data class OpenAIResponse<T> (
    val errorCode: ErrorCodeEnum,
    val data : T,
    val message: String? = null,
)

enum class ErrorCodeEnum(val code: Int) {
    Success(0),
    InternalError (9999)
}

