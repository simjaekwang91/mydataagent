package com.openai.mydataagent.adapter.redis

import com.openai.mydataagent.adapter.out.redis.RedisImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RedisTest {
    @Autowired
    private lateinit var redisImpl: RedisImpl

    @BeforeEach
    fun setUp() {
        redisImpl.setCacheData("testkey", "testValue")
    }

    @Test
    fun redisGetTest(){
        val redisGetValue = redisImpl.getCacheData("testkey", String::class.java)
        Assertions.assertEquals("testValue", redisGetValue)
    }

    @Test
    fun redisSetTest() {
        redisImpl.setCacheData("testkey2", "testValue2")
        val redisGetValue = redisImpl.getCacheData("testkey2", String::class.java)
        Assertions.assertEquals("testValue2", redisGetValue)
    }

    @Test
    fun redisGetFailTest() {
        val redisGetValue = redisImpl.getCacheData("testkey3", String::class.java)
        Assertions.assertNull(redisGetValue)
        Assertions.assertNotEquals("testValue", redisGetValue)
    }
}
