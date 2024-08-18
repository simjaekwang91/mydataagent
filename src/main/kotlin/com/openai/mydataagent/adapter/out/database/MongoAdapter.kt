package com.openai.mydataagent.adapter.out.database

import com.openai.mydataagent.adapter.`in`.restapi.exception.MongoCustomException
import com.openai.mydataagent.adapter.`in`.restapi.model.ErrorCodeEnum
import com.openai.mydataagent.adapter.out.database.mapper.ConversationHistoryMapper
import com.openai.mydataagent.application.port.out.ConversationHistoryPort
import com.openai.mydataagent.domain.ConversationHistoryDto
import org.springframework.stereotype.Repository

/**
 * Mongo adapter
 * 전체 대화내역 저장
 * @property conversationRepository
 * @constructor Create empty Mongo adapter
 */
@Repository
class MongoAdapter(private val conversationRepository: ConversationRepository): ConversationHistoryPort {
    override fun getAllConversationList(userId: String): List<ConversationHistoryDto>? {
        return try {
            conversationRepository.findByUserId(userId).takeIf { it.isNotEmpty() }?.map {
                ConversationHistoryMapper.toDto(it)
            }
        } catch (e: Exception) {
            throw MongoCustomException(ErrorCodeEnum.MongoError, e.cause, e.message)
        }
    }

    override fun getConversationList(userId: String, roomId: String): ConversationHistoryDto? {
        return try {
            conversationRepository.findByUserIdAndRoomId(userId, roomId).firstOrNull()?.let {
                ConversationHistoryMapper.toDto(it)
            }
        } catch (e: Exception) {
            throw MongoCustomException(ErrorCodeEnum.MongoError, e.cause, e.message)
        }
    }

    override fun setConversationList(
        conversationHistoryDto: ConversationHistoryDto
    ): Boolean {
        return try {
            conversationRepository.save(ConversationHistoryMapper.fromDto(conversationHistoryDto)).id?.isNotBlank() ?: false
        } catch (e: Exception) {
            throw MongoCustomException(ErrorCodeEnum.MongoError, e.cause, e.message)
        }
    }
}
