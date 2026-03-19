package com.example.max.domain.use.cases

import com.example.max.data.repository.MaxRepository
import javax.inject.Inject

class ClearAllMessagesUseCase @Inject constructor(
    private val repository: MaxRepository
) {
    suspend fun clearAllMessages(){
        repository.clearAllMessage()
    }

}