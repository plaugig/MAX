package com.example.max.ui.item

data class ItemMessageData(
    val id: String,
    val text: String,
    val time: String,
    val isMine: Boolean,
    val isSent: Boolean,
    val imageUrl: String?
)