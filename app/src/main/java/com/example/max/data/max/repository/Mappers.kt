package com.example.max.data.max.repository

import com.example.max.data.database.entities.MessageEntity
import com.example.max.data.database.entities.UserEntity
import com.example.max.data.max.MessageData
import com.example.max.data.max.UserData
import com.example.max.data.remote.data.MessageFirebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun MessageEntity.toDomain(currentUserId: String): MessageData {
    return MessageData(
        id = this.id,
        text = this.text,
        senderId = this.senderId,
        time = formatTimes(this.timestamp),
        imageUrl = this.imageUrl,
        isMine = this.senderId == currentUserId ,
        isSent = this.isSent
    )
}

fun MessageData.toFirebase(): MessageFirebase {
    return MessageFirebase(
        id = this.id,
        senderId = this.senderId,
        text = this.text,
        time = System.currentTimeMillis(),
        imageUrl = this.imageUrl
    )
}

fun MessageFirebase.toEntity(chatId: String): MessageEntity {
    return MessageEntity(
        id = this.id,
        chatId = chatId,
        senderId = this.senderId,
        text = this.text,
        timestamp = this.time,
        imageUrl = this.imageUrl,
        isSent = true,
    )
}

fun UserEntity.toDomain(): UserData{
    return UserData(
        userId = this.userId,
        name = this.name,
        avatarUrl = this.avatarUrl,
        lastMessage = this.lastMessage
    )
}

fun UserData.toEntity(): UserEntity {
    return UserEntity(
        userId = this.userId,
        name = this.name,
        avatarUrl = this.avatarUrl ?: "",
        lastMessage = this.lastMessage
    )
}

fun List<UserEntity>.toDomainList(): List<UserData> {
    return this.map { it.toDomain() }
}

private fun formatTimes(time: Long): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(time))
}


