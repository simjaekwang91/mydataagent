package com.openai.mydataagent.adapter.out.cache

import com.openai.mydataagent.adapter.`in`.restapi.exception.RedisCustomException
import com.openai.mydataagent.adapter.`in`.restapi.model.ErrorCodeEnum
import com.openai.mydataagent.adapter.out.JsonMapper
import com.openai.mydataagent.application.port.out.CacheConversationPort
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

/**
 * Redis adapter
 * 대화 내역 최근 10개 저장
 * @property redisTemplate
 * @constructor Create empty Redis adapter
 */
@Repository
class RedisAdapter(private val redisTemplate: RedisTemplate<String, String>): CacheConversationPort {

    override fun <T> setCacheData(key: String, data: T) {
        try {
            validateKey(key)
            redisTemplate.opsForValue().set(key, JsonMapper.getMapper().writeValueAsString(data))
        } catch (e: Exception) {
            throw RedisCustomException(ErrorCodeEnum.RedisError, e.cause, e.message)
        }
    }

    override fun <T> getCacheData(key: String, clazz: Class<T>): T? {
        return try {
            validateKey(key)
            redisTemplate.opsForValue().get(key)?.let {
                JsonMapper.getMapper().readValue(it, clazz)
            }
        } catch (e: Exception) {
            throw RedisCustomException(ErrorCodeEnum.RedisError, e.cause, e.message)
        }
    }

    private fun validateKey(key: String) {
        require(key.isNotBlank()) { "Redis key 는 비어있을수 없습니다." }
    }
}
