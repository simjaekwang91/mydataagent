package com.openai.mydataagent.application.port.out

import java.time.Instant

data class ChattingRoomListResponseCommand(
    val chattingRoomList: List<ChattingRoom>,
)

data class ChattingRoom(
    val roomId: String,
    val title: String,
    val createTime: Instant,
    val updateTime: Instant,
)
