package com.example.max.ui.common

data class ItemUserData(
    val id: String,
    val name: String,
    val lastMessage: String?,
    val avatarUrl : String? = null,
    val isMe: Boolean = false,
    val threadId: String? = null
)
