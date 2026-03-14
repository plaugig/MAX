package com.example.max.domain.use.cases

import com.example.max.data.database.preferences.UserPrefs
import com.example.max.data.UserData
import com.example.max.data.repository.MaxRepository
import com.example.max.ui.common.ItemUserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProfileUserUseCase @Inject constructor(
    private val repository: MaxRepository,
    private val userPrefs: UserPrefs
) {
    operator fun invoke(chatId: String): Flow<ItemUserData?>{
         return repository.getProfile(chatId).map { user ->
             user?.copy(isMe = user.userId == userPrefs.getMyID())
         }.map { domainUser ->
             domainUser?.let {
                 ItemUserData(
                     id = it.userId,
                     name = it.name,
                     avatarUrl = it.avatarUrl,
                     lastMessage = "",
                     isMe = it.isMe
                 )
             }
         }
    }
}