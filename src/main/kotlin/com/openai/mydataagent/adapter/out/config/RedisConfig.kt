package com.openai.mydataagent.adapter.out.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import redis.embedded.RedisServer

/**
 * Redis config
 * 실제 운영 환경에서는 운영 Redis를 사용하고
 * 여기서는 테스트용 EmbedRedis를 사용
 * @property redisProperties
 * @constructor Create empty Redis config
 */
@Configuration
class RedisConfig(private val redisProperties: RedisProperties) {

    private lateinit var redisServer: RedisServer
    @PostConstruct
    fun startRedis() {
        redisServer = RedisServer(redisProperties.port)
        redisServer.start()
    }

    @PreDestroy
    fun stopRedis() {
        redisServer.stop()
    }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(redisProperties.host, redisProperties.port)
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        return StringRedisTemplate(redisConnectionFactory)
    }
}
