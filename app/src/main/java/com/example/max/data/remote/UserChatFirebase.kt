package com.example.max.data.remote

data class UserChatFirebase(
    val threadId: String = "",
    val peerUserId: String = "",
    val peerName: String = "",
    val peerAvatarUrl: String = "",
    val lastMessage: String = "",
    val lastTimestamp: Long = 0L
)
