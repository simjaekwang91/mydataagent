package com.openai.mydataagent.adapter.`in`.restapi.model

data class OpenAIResponse<T> (
    val errorCode: ErrorCodeEnum,
    val data : T,
    val message: String? = null,
)

enum class ErrorCodeEnum(val code: Int) {
    Success(0),
    BusinessError(5000),
    OpenAIError(6000),
    MongoError(7000),
    RedisError(8000),
    InternalError (9999)
}

