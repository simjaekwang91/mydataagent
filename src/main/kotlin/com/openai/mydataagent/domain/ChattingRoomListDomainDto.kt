package com.openai.mydataagent.domain

import java.time.Instant

data class ChattingRoomListDomainDto(
    val chattingRoomList: List<ChattingRoom>,
)

data class ChattingRoom(
    val roomId: String,
    val title: String,
    val createTime: Instant,
    val updateTime: Instant,
)
