package com.example.max.domain

import com.example.max.data.max.MessageData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase
) {
    suspend fun sendMessage(text: String, myUid: String) {
        return sendMessageUseCase.invoke(text, myUid)
    }

    fun getMessage(myUid: String): Flow<List<MessageData>> {
        return getMessagesUseCase.invoke(myUid)
    }
}