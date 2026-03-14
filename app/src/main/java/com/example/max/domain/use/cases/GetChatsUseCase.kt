package com.example.max.domain.use.cases

import com.example.max.data.UserData
import com.example.max.data.repository.MaxRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val repository: MaxRepository
) {
    operator fun invoke(): Flow<List<UserData>> {
        return repository.getAllUser()
    }
}