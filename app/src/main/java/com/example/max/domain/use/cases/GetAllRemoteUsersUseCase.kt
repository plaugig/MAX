package com.example.max.domain.use.cases

import com.example.max.data.max.UserData
import com.example.max.data.max.repository.MaxRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRemoteUsersUseCase @Inject constructor(
    private val repository: MaxRepository
) {
    operator fun invoke(): Flow<List<UserData>> {
        return repository.getAllRemoteUsers()
    }
}