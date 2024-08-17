package com.openai.mydataagent.adapter.out.database

import com.openai.mydataagent.adapter.out.database.entity.ConversationEntity
import com.openai.mydataagent.adapter.out.database.entity.ConversationHistoryEntity
import com.openai.mydataagent.adapter.out.database.mapper.ConversationHistoryMapper
import java.time.Instant
import java.util.UUID
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
class MongoTest {
    @Autowired
    private lateinit var mongoAdapter : MongoAdapter

    private lateinit var roomId: String

    @BeforeEach
    fun setUp() {
        val conversation = ConversationEntity("Hello", "Hi", Instant.now())
        roomId =  UUID.randomUUID().toString()
        val conversationHistoryEntity = ConversationHistoryEntity(userId = "user1",
            roomId = roomId,
            conversationList = listOf(conversation)
            )

        mongoAdapter.setConversationList(ConversationHistoryMapper.toDto(conversationHistoryEntity))
    }

    @Test
    fun findSuccessTest() {
        val result = mongoAdapter.getConversationList("user1", roomId)

        Assertions.assertNotNull(result)
        Assertions.assertEquals("user1", result?.userId)
        Assertions.assertEquals("Hello", result?.conversationList?.get(0)?.question)
        Assertions.assertEquals("Hi", result?.conversationList?.get(0)?.response)

    }
}
