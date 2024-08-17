package com.openai.mydataagent.adapter.`in`.restapi.config

import com.openai.mydataagent.adapter.`in`.restapi.exception.MongoCustomException
import com.openai.mydataagent.adapter.`in`.restapi.exception.OpenAICustomException
import com.openai.mydataagent.adapter.`in`.restapi.exception.RedisCustomException
import com.openai.mydataagent.adapter.`in`.restapi.model.OpenAIResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MongoCustomException::class)
    fun handleMongoCustomException(ex: MongoCustomException): ResponseEntity<OpenAIResponse<Nothing?>> {
        val response = OpenAIResponse(
            errorCode = ex.customErrorCode,
            data = null,
            message = ex.message
        )

        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(RedisCustomException::class)
    fun handleRedisCustomException(ex: RedisCustomException): ResponseEntity<OpenAIResponse<Nothing?>> {
        val response = OpenAIResponse(
            errorCode = ex.customErrorCode,
            data = null,
            message = ex.message
        )

        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(OpenAICustomException::class)
    fun handleOpenAICustomException(ex: OpenAICustomException): ResponseEntity<OpenAIResponse<Nothing?>> {
        val response = OpenAIResponse(
            errorCode = ex.customErrorCode,
            data = null,
            message = ex.message
        )

        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
