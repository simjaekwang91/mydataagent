package com.openai.mydataagent.domain

import java.time.Instant
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DomainTest {

    private lateinit var conversationHistory : ConversationHistoryDto
    private lateinit var conversationDto1: ConversationDto
    private lateinit var conversationDto2: ConversationDto
    @BeforeEach
    fun setUp() {
        conversationDto1 = ConversationDto("Question1", "Response1", Instant.now())
        conversationDto2 = ConversationDto("Question2", "Response2", Instant.now())
        conversationHistory = ConversationHistoryDto(
            userId = "user1",
            roomId = "room1",
            conversationList = mutableListOf(conversationDto1, conversationDto2)
        )
    }
    @Test
    fun removeConversationHistoryTest() {
        conversationHistory.removeConversationHistory()

        // Then
        Assertions.assertEquals(1, conversationHistory.conversationList.size)
        Assertions.assertEquals(conversationDto2, conversationHistory.conversationList.first())
    }

    @Test
    fun addConversationHistoryTest() {
        val conversationDto = ConversationDto("Question3", "Response3", Instant.now())

        conversationHistory.addConversationHistory(conversationDto)

        Assertions.assertEquals(3, conversationHistory.conversationList.size)
        Assertions.assertEquals(conversationDto, conversationHistory.conversationList.last())
    }
}
