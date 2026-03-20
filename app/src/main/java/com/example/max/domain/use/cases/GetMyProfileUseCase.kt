package com.example.max.domain.use.cases

import com.example.max.data.database.preferences.UserPrefs
import com.example.max.data.UserData
import com.example.max.data.repository.MaxRepository
import com.example.max.ui.common.ItemUserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val repository: MaxRepository,
    private val userPrefs: UserPrefs
) {
    operator fun invoke() : Flow<ItemUserData?> {
        return userPrefs.getMyID().flatMapLatest { myId ->
            repository.getProfile(myId).map { user ->
                user?.let {
                    ItemUserData(
                        id = it.userId,
                        name = it.name,
                        lastMessage = "",
                        avatarUrl = it.avatarUrl,
                        isMe = true,
                        threadId = null
                    )
                }
            }
        }
    }
}