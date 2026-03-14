package com.example.max.domain.use.cases

import com.example.max.data.UserData
import com.example.max.data.repository.MaxRepository
import javax.inject.Inject

class CacheContactUseCase @Inject constructor(
    private val repository: MaxRepository
) {
    suspend operator fun invoke(user: UserData) {
        repository.cacheContact(user)
    }
}
