package com.openai.mydataagent.adapter.out.redis

import com.openai.mydataagent.adapter.out.cache.RedisAdapter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
class RedisTest {
    @Autowired
    private lateinit var redisAdapter: RedisAdapter

    @BeforeEach
    fun setUp() {
        redisAdapter.setCacheData("testkey", "testValue")
    }

    @Test
    fun redisGetTest(){
        val redisGetValue = redisAdapter.getCacheData("testkey", String::class.java)
        Assertions.assertEquals("testValue", redisGetValue)
    }

    @Test
    fun redisSetTest() {
        redisAdapter.setCacheData("testkey2", "testValue2")
        val redisGetValue = redisAdapter.getCacheData("testkey2", String::class.java)
        Assertions.assertEquals("testValue2", redisGetValue)
    }

    @Test
    fun redisGetFailTest() {
        val redisGetValue = redisAdapter.getCacheData("testkey3", String::class.java)
        Assertions.assertNull(redisGetValue)
        Assertions.assertNotEquals("testValue", redisGetValue)
    }
}
