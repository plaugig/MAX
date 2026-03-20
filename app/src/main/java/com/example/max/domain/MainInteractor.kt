package com.example.max.domain

import com.example.max.data.UserData
import com.example.max.domain.use.cases.CacheContactUseCase
import com.example.max.domain.use.cases.ClearAllMessagesUseCase
import com.example.max.domain.use.cases.GetAllRemoteUsersUseCase
import com.example.max.domain.use.cases.GetChatsUseCase
import com.example.max.domain.use.cases.GetCurrentUserIdUserCase
import com.example.max.domain.use.cases.GetMessagesUseCase
import com.example.max.domain.use.cases.GetMyProfileUseCase
import com.example.max.domain.use.cases.GetProfileUserUseCase
import com.example.max.domain.use.cases.SendMessageUseCase
import com.example.max.domain.use.cases.SingUpUseCase
import com.example.max.domain.use.cases.SingInUseCase
import com.example.max.ui.common.ItemMessageData
import com.example.max.ui.common.ItemUserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getChatsUseCase: GetChatsUseCase,
    private val cacheContactUseCase: CacheContactUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getProfileUserUseCase: GetProfileUserUseCase,
    private val singUpUseCase: SingInUseCase,
    private val singInUseCase: SingUpUseCase,
    private val getAllRemoteUsersUseCase: GetAllRemoteUsersUseCase,
    private val getCurrentUserIdUserCase: GetCurrentUserIdUserCase,
    private val clearAllMessagesUseCase: ClearAllMessagesUseCase
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

    suspend fun cacheContact(user: UserData) {
        cacheContactUseCase(user)
    }

    fun getMyProfile(): Flow<ItemUserData?> {
        return getMyProfileUseCase()
    }

    fun getChatProfile(id: String): Flow<ItemUserData?> {
        return getProfileUserUseCase(id)
    }

    suspend fun singUp(email: String, password: String, name: String){
        singInUseCase.singUp(email,password,name)
    }

    suspend fun singIn(email: String, password: String){
        singUpUseCase.singIn(email,password)
    }

    fun getCurrentUserId(): Flow<String> {
        return getCurrentUserIdUserCase.getCurrentUserId()
    }

    suspend fun clearAllMessages(){
        clearAllMessagesUseCase.clearAllMessages()
    }
}
