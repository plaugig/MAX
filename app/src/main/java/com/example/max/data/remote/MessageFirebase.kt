package com.example.max.data.remote

data class MessageFirebase(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val time: Long = 0L,
    val imageUrl: String? = ""
)
