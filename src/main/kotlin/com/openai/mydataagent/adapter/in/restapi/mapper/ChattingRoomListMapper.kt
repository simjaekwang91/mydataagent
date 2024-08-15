package com.openai.mydataagent.adapter.`in`.restapi.mapper

import com.openai.mydataagent.adapter.`in`.restapi.dto.ChattingRoomDto
import com.openai.mydataagent.adapter.`in`.restapi.dto.ChattingRoomListResponseDto
import com.openai.mydataagent.application.port.out.ChattingRoom
import com.openai.mydataagent.application.port.out.ChattingRoomListResponseCommand

object ChattingRoomListMapper {
    fun toCommand(command: ChattingRoomListResponseDto): ChattingRoomListResponseCommand {
        val chattingRoomList = command.chattingRoomList.map { toCommand(it) }
        return ChattingRoomListResponseCommand(chattingRoomList)
    }

    fun fromCommand(dto: ChattingRoomListResponseCommand): ChattingRoomListResponseDto {
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
