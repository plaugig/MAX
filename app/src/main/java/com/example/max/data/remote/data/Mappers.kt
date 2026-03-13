package com.example.max.data.remote.data

import com.example.max.data.database.entities.MessageEntity


fun MessageFirebase.toEntity(chatId: String): MessageEntity {
    return MessageEntity(
        id = this.id,
        senderId = this.senderId,
        text = this.text,
        timestamp = this.time,
        imageUrl = this.imageUrl,
        isSent = true,
        chatId = chatId
    )
}

