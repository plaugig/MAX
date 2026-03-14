package com.example.max.domain.use.cases

import com.example.max.data.repository.MaxRepository
import com.example.max.ui.common.ItemMessageData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: MaxRepository
) {
    operator fun invoke(chatId: String, myUid: String): Flow<List<ItemMessageData>> {
        return repository.getMessage(chatId, myUid).map { messages ->
            messages.map { message ->
                ItemMessageData(
                    id = message.id,
                    text = message.text,
                    time = formatTime(message.time.toLongOrNull() ?: 0L),
                    isMine = message.senderId == myUid,
                    isSent = message.isSent,
                    imageUrl = message.imageUrl
                )
            }
        }
    }

    private fun formatTime(millis: Long): String {
        val sdf = SimpleDateFormat("HH.mm", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}