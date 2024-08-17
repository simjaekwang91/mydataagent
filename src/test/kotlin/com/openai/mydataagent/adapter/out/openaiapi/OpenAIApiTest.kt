package com.openai.mydataagent.adapter.out.openaiapi

import com.openai.mydataagent.adapter.`in`.restapi.exception.OpenAICustomException
import com.openai.mydataagent.adapter.out.aiapi.OpenAIApiAdapter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertNotNull

@SpringBootTest
class OpenAIApiTest {

    @Autowired
    private lateinit var openAIApiAdapter: OpenAIApiAdapter

    @Test
    fun callOpenAiAPITest(){
        val response = openAIApiAdapter.getAIResponse(listOf("너는 누구니?"), true)
        assertNotNull(response)
    }

    @Test
    fun callFailTest() {
        assertThrows<OpenAICustomException> {
            openAIApiAdapter.getAIResponse(listOf(), true)
        }
    }
}
