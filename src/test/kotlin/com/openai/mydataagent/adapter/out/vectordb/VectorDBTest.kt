package com.openai.mydataagent.adapter.out.vectordb

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertTrue

@SpringBootTest
class VectorDBTest {

    @Autowired
    private lateinit var weaviateRagAdapter: WeaviateRagAdapter

    @Test
    fun searchDataTest(){
        val result = weaviateRagAdapter.searchSimilarVectors("마이데이터")
        assertTrue(result.isNotEmpty(), "성공")
    }
}
