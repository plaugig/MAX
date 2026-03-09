package com.example.max.domain.use.cases

import com.example.max.data.max.UserData
import com.example.max.data.max.repository.MaxRepository
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val repository: MaxRepository
) {
    suspend operator fun invoke(user: UserData) {
        repository.saveProfile(user)
    }
}