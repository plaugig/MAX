package com.example.max.data.max.repository

import com.example.max.data.database.entities.MessageEntity
import com.example.max.data.database.entities.UserEntity
import com.example.max.data.max.MessageData
import com.example.max.data.max.UserData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun MessageEntity.toDomain(currentUserId: String): MessageData {
    return MessageData(
        id = this.id,
        text = this.text,
        senderId = this.senderId,
        time = formatTimes(this.time),
        imageUrl = this.imageUrl,
        isMine = this.senderId == "MY_ID",
        isSent = this.isSent
    )
}

fun UserEntity.toDomain(): UserData{
    return UserData(
        userId = this.userId,
        name = this.name,
        avatarUrl = this.avatarUrl,
        isOnline = this.isOnline
    )
}

private fun formatTimes(time: Long): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(time))
}