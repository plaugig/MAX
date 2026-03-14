package com.example.max.domain

import com.example.max.data.MessageData
import com.example.max.data.UserData
import com.example.max.domain.use.cases.GetAllRemoteUsersUseCase
import com.example.max.domain.use.cases.GetChatsUseCase
import com.example.max.domain.use.cases.GetMessagesUseCase
import com.example.max.domain.use.cases.GetMyProfileUseCase
import com.example.max.domain.use.cases.GetProfileUserUseCase
import com.example.max.domain.use.cases.SaveProfileUseCase
import com.example.max.domain.use.cases.SendMessageUseCase
import com.example.max.ui.common.ItemUserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getChatsUseCase: GetChatsUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getProfileUserUseCase: GetProfileUserUseCase,
    private val getAllRemoteUsersUseCase: GetAllRemoteUsersUseCase
) {
    suspend fun sendMessage(
        text: String?,
        chatId: String,
        imageUrl: String? = null
    ) {
        return sendMessageUseCase.invoke(text, chatId, imageUrl)
    }

    fun getMessage(chatId: String,myUid: String): Flow<List<MessageData>> {
        return getMessagesUseCase.invoke(chatId,myUid)
    }

    fun getChats(): Flow<List<UserData>> {
        return getChatsUseCase.invoke()
    }

    fun getAllRemoteUsers(): Flow<List<ItemUserData>> {
        return getAllRemoteUsersUseCase().map { userDataList ->
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

    suspend fun saveProfile(user: UserData) = saveProfileUseCase(user)

    fun getMyProfile(id: String): Flow<UserData?> {
        return getMyProfileUseCase.invoke(id)
    }

    fun getChatProfile(id: String): Flow<UserData?> {
        return getProfileUserUseCase.invoke(id)
    }
}