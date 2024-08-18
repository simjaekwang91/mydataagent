package com.openai.mydataagent.adapter.`in`.restapi.config

import com.openai.mydataagent.adapter.`in`.restapi.exception.MongoCustomException
import com.openai.mydataagent.adapter.`in`.restapi.exception.OpenAICustomException
import com.openai.mydataagent.adapter.`in`.restapi.exception.RedisCustomException
import com.openai.mydataagent.adapter.`in`.restapi.model.OpenAIResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Global exception handler
 *
 * @constructor Create empty Global exception handler
 */
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * mongo custom exception
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MongoCustomException::class)
    fun handleMongoCustomException(ex: MongoCustomException): ResponseEntity<OpenAIResponse<Nothing?>> {
        val response = OpenAIResponse(
            errorCode = ex.customErrorCode,
            data = null,
            message = ex.message
        )

        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * redis custom exception
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(RedisCustomException::class)
    fun handleRedisCustomException(ex: RedisCustomException): ResponseEntity<OpenAIResponse<Nothing?>> {
        val response = OpenAIResponse(
            errorCode = ex.customErrorCode,
            data = null,
            message = ex.message
        )

        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * openAI custom exception
     *
     * @param ex
     * @return
     */
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
