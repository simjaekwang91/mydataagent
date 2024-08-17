package com.openai.mydataagent.adapter.`in`.restapi.exception

import com.openai.mydataagent.adapter.`in`.restapi.model.ErrorCodeEnum

class OpenAICustomException(
    val customErrorCode: ErrorCodeEnum,
    override val cause: Throwable?,
    override val message: String?
) : RuntimeException(message, cause)
