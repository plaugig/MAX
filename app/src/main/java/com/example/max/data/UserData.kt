package com.example.max.data

data class UserData(
    val userId: String,
    val name: String,
    val avatarUrl: String?,
    val isMe: Boolean = false,
    val lastMessage: String? = null,
    val threadId: String? = null
)
