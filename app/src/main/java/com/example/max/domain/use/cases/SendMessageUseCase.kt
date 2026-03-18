package com.example.max.domain.use.cases

import com.example.max.data.MessageData
import com.example.max.data.repository.MaxRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: MaxRepository
) {
    suspend operator fun invoke(
        text: String?,
        chatId: String,
        peerUserId: String,
        imageUrl: String? = null
    ) {
        if (text.isNullOrBlank() && imageUrl == null) return

        val myUid = repository.getCurrentUserIdFromPrefs().first()

        val newMessage = MessageData(
            id = UUID.randomUUID().toString(),
            senderId = myUid,
            text = text ?: "",
            time = System.currentTimeMillis().toString(),
            imageUrl = imageUrl,
            isSent = false,
            isMine = true
        )
        repository.sendMessage(newMessage, chatId, peerUserId)
    }
}
