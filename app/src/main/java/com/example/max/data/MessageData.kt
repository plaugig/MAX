package com.example.max.data

data class MessageData(
    val id: String,
    val senderId: String,
    val text: String,
    val time: String,
    val imageUrl: String?,
    val isSent: Boolean,
    val isMine: Boolean

)
