package com.example.max.domain.use.cases

import com.example.max.data.database.preferences.UserPrefs
import com.example.max.data.max.UserData
import com.example.max.data.max.repository.MaxRepository
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val repository: MaxRepository,
    private val userPrefs: UserPrefs
) {
    suspend operator fun invoke(user: UserData) {
        repository.saveProfile(user)
        userPrefs.saveMyId(user.userId)
    }
}