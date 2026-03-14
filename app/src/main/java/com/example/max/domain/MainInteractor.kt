package com.example.max.domain

import com.example.max.data.max.MessageData
import com.example.max.data.max.UserData
import com.example.max.domain.use.cases.GetAllRemoteUsersUseCase
import com.example.max.domain.use.cases.GetChatsUseCase
import com.example.max.domain.use.cases.GetMessagesUseCase
import com.example.max.domain.use.cases.GetMyProfileUseCase
import com.example.max.domain.use.cases.GetProfileUserUseCase
import com.example.max.domain.use.cases.SaveProfileUseCase
import com.example.max.domain.use.cases.SendMessageUseCase
import kotlinx.coroutines.flow.Flow
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

    fun getAllRemoteUsers(): Flow<List<UserData>> = getAllRemoteUsersUseCase()

    suspend fun saveProfile(user: UserData) = saveProfileUseCase(user)

    fun getMyProfile(id: String): Flow<UserData?> {
        return getMyProfileUseCase.invoke(id)
    }

    fun getChatProfile(id: String): Flow<UserData?> {
        return getProfileUserUseCase.invoke(id)
    }
}