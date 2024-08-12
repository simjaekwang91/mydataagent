package com.openai.mydataagent.adapter.out.Properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@Component
@ConfigurationProperties(prefix = "spring.data.redis")
data class RedisProperties(
    val host: String = "localhost",
    val port: Int = 6379
)
