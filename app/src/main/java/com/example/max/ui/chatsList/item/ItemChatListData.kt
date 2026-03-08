package com.example.max.ui.chatsList.item

data class ItemChatListData(
    val id: String,
    val name: String,
    val lastMessage: String,
    val avatarUrl : String? = null
)