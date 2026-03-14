package com.example.max.domain

object DirectChatThreadId {
    fun from(firstUserId: String, secondUserId: String): String {
        return listOf(firstUserId, secondUserId)
            .sorted()
            .joinToString(separator = "_")
    }
}
