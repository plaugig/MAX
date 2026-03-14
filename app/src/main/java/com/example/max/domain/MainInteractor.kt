package com.example.max.domain

import com.example.max.data.UserData
import com.example.max.domain.use.cases.CacheContactUseCase
import com.example.max.domain.use.cases.GetAllRemoteUsersUseCase
import com.example.max.domain.use.cases.GetChatsUseCase
import com.example.max.domain.use.cases.GetMessagesUseCase
import com.example.max.domain.use.cases.GetMyProfileUseCase
import com.example.max.domain.use.cases.GetProfileUserUseCase
import com.example.max.domain.use.cases.SaveProfileUseCase
import com.example.max.domain.use.cases.SendMessageUseCase
import com.example.max.ui.common.ItemMessageData
import com.example.max.ui.common.ItemUserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getChatsUseCase: GetChatsUseCase,
    private val cacheContactUseCase: CacheContactUseCase,
    private val saveProfileUseCase: SaveProfileUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getProfileUserUseCase: GetProfileUserUseCase,
    private val getAllRemoteUsersUseCase: GetAllRemoteUsersUseCase
) {
    suspend fun sendMessage(
        text: String?,
        chatId: String,
        peerUserId: String,
        imageUrl: String? = null
    ) {
        sendMessageUseCase(text, chatId, peerUserId, imageUrl)
    }

    fun getMessage(chatId: String, myUid: String): Flow<List<ItemMessageData>> {
        return getMessagesUseCase(chatId, myUid)
    }

    fun getChats(): Flow<List<UserData>> {
        return getChatsUseCase()
    }

    fun getAllRemoteUsers(): Flow<List<ItemUserData>> {
        return getAllRemoteUsersUseCase()
    }

    suspend fun saveProfile(user: UserData) {
        saveProfileUseCase(user)
    }

    suspend fun cacheContact(user: UserData) {
        cacheContactUseCase(user)
    }

    fun getMyProfile(id: String): Flow<UserData?> {
        return getMyProfileUseCase(id)
    }

    fun getChatProfile(id: String): Flow<ItemUserData?> {
        return getProfileUserUseCase(id)
    }

    fun getCurrentUserId(): String {
        return saveProfileUseCase.getCurrentUserId()
    }
}
