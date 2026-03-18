package com.example.max.domain.use.cases

import com.example.max.data.database.preferences.UserPrefs
import com.example.max.data.UserData
import com.example.max.data.repository.MaxRepository
import com.example.max.ui.common.ItemUserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProfileUserUseCase @Inject constructor(
    private val repository: MaxRepository,
    private val userPrefs: UserPrefs
) {
    operator fun invoke(chatId: String): Flow<ItemUserData?>{
        val profileFlow = repository.getProfile(chatId)
        val myId = userPrefs.getMyID()

         return profileFlow.combine(myId){ user, myId ->
             user?.let {
                 val isMe = it.userId == myId
                 ItemUserData(
                     id = it.userId,
                     name = it.name,
                     avatarUrl = it.avatarUrl,
                     lastMessage = "",
                     isMe = isMe
                 )
             }
         }
    }
}