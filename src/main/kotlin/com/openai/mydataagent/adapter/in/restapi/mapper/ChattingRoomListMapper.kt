package com.openai.mydataagent.adapter.`in`.restapi.mapper

import com.openai.mydataagent.adapter.`in`.restapi.dto.ChattingRoomDto
import com.openai.mydataagent.adapter.`in`.restapi.dto.ChattingRoomListResponseDto
import com.openai.mydataagent.domain.ChattingRoom
import com.openai.mydataagent.domain.ChattingRoomListDomainDto

object ChattingRoomListMapper {
    fun toCommand(command: ChattingRoomListResponseDto): ChattingRoomListDomainDto {
        val chattingRoomList = command.chattingRoomList.map { toCommand(it) }
        return ChattingRoomListDomainDto(chattingRoomList)
    }

    fun fromCommand(dto: ChattingRoomListDomainDto): ChattingRoomListResponseDto {
        val chattingRoomList = dto.chattingRoomList.map { toDto(it) }
        return ChattingRoomListResponseDto(chattingRoomList)

    }

    private fun toDto(chattingRoom: ChattingRoom): ChattingRoomDto {
        return ChattingRoomDto(
            roomId = chattingRoom.roomId,
            title = chattingRoom.title,
            createTime = chattingRoom.createTime,
            updateTime = chattingRoom.updateTime
        )
    }

    private fun toCommand(dto: ChattingRoomDto): ChattingRoom {
        return ChattingRoom(
            roomId = dto.roomId,
            title = dto.title,
            createTime = dto.createTime,
            updateTime = dto.updateTime
        )
    }
}
