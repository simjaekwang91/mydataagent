package com.openai.mydataagent.adapter.out.cache

import com.openai.mydataagent.adapter.out.JsonMapper
import com.openai.mydataagent.application.port.out.CacheConversactionPort
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class RedisImpl(private val redisTemplate: RedisTemplate<String, String>): CacheConversactionPort {

    override fun <T> setCacheData(key: String, data: T) {
        redisTemplate.opsForValue().set(key, JsonMapper.getMapper().writeValueAsString(data))
    }

    override fun <T> getCacheData(key: String, clazz: Class<T>): T? {
        return redisTemplate.opsForValue().get(key)?.let {
            JsonMapper.getMapper().readValue(it, clazz)
        }
    }
}
