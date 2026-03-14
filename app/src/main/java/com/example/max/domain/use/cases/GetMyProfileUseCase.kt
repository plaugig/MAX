package com.example.max.domain.use.cases

import com.example.max.data.database.preferences.UserPrefs
import com.example.max.data.UserData
import com.example.max.data.repository.MaxRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val repository: MaxRepository,
    private val userPrefs: UserPrefs
) {
    operator fun invoke(id: String) : Flow<UserData?> {
        val myId = userPrefs.getMyID()
        return repository.getProfile(id).map { user ->
            user?.copy(isMe = true)
        }
    }
}