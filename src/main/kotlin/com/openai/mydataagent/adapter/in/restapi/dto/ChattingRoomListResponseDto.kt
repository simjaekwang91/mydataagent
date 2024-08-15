package com.openai.mydataagent.adapter.`in`.restapi.dto

import java.time.Instant

data class ChattingRoomListResponseDto(
    val chattingRoomList: List<ChattingRoomDto>,
)

data class ChattingRoomDto(
    val roomId: String,
    val title: String,
    val createTime: Instant,
    val updateTime: Instant,
)
