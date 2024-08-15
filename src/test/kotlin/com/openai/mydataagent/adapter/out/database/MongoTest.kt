package com.openai.mydataagent.adapter.out.database

import com.openai.mydataagent.adapter.out.database.entity.Conversation
import com.openai.mydataagent.adapter.out.database.entity.ConversationHistoryEntity
import java.util.UUID
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MongoTest {
    @Autowired
    private lateinit var mongoImpl : MongoImpl

    private lateinit var roomId: String

    @BeforeEach
    fun setUp() {
        val conversation = Conversation("Hello", "Hi")
        roomId =  UUID.randomUUID().toString()
        val conversationHistory = ConversationHistoryEntity(userId = "user1",
            roomId = roomId,
            conversationList = listOf(conversation)
            )

        mongoImpl.setConversationList(conversationHistory)
    }

    @Test
    fun findSuccessTest() {
        val result = mongoImpl.getConversationList("user1", roomId)

        Assertions.assertNotNull(result)
        Assertions.assertEquals(1, result.count())
        Assertions.assertEquals("Hello", result.first().conversationList.first().question)
        Assertions.assertEquals("Hi", result.first().conversationList.first().response)

    }
}
