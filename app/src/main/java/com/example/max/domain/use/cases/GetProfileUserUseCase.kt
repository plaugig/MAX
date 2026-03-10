package com.example.max.domain.use.cases

import com.example.max.data.database.preferences.UserPrefs
import com.example.max.data.max.UserData
import com.example.max.data.max.repository.MaxRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProfileUserUseCase @Inject constructor(
    private val repository: MaxRepository,
    private val userPrefs: UserPrefs
) {
    operator fun invoke(chatId: String): Flow<UserData?>{
         return repository.getProfile(chatId).map { user ->
             user?.copy(isMe = user.userId == userPrefs.getMyID())
         }
    }
}