package com.example.max.domain.use.cases

import com.example.max.data.MessageData
import com.example.max.data.repository.MaxRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

    class GetMessagesUseCase @Inject constructor(
        private val repository: MaxRepository
    ) {
        operator fun invoke(chatId: String, myUid: String) : Flow<List<MessageData>> {
            return repository.getMessage(chatId,myUid)
        }
    }