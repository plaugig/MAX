package com.example.max.domain.use.cases

import com.example.max.data.max.repository.MaxRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: MaxRepository
) {
    suspend operator fun invoke(text: String, myUid: String){
        if (text.isBlank()) return
        repository.sendMessage(text, myUid)
    }
}