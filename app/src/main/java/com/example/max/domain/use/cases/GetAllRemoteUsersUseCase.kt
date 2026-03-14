package com.example.max.domain.use.cases

import com.example.max.data.repository.MaxRepository
import com.example.max.ui.common.ItemUserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllRemoteUsersUseCase @Inject constructor(
    private val repository: MaxRepository
) {
    operator fun invoke(): Flow<List<ItemUserData>> {
        return repository.getAllRemoteUsers().map { userDataList ->
            userDataList.map { user ->
                ItemUserData(
                    id = user.userId,
                    name = user.name,
                    avatarUrl = user.avatarUrl,
                    lastMessage = null
                )
            }
        }
    }
}