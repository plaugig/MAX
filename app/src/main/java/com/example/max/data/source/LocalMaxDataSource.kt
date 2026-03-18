package com.example.max.data.source

import com.example.max.data.database.AppDatabase
import com.example.max.data.database.entities.MessageEntity
import com.example.max.data.database.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocalMaxDataSource @Inject constructor(
    private val database: AppDatabase
) {
    fun getMessages(chatId: String): Flow<List<MessageEntity>> {
        return database.messageDao().getMessagesByChatId(chatId)
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    suspend fun insertMessage(message: MessageEntity) {
        database.messageDao().insertMessage(message)
    }

    suspend fun markMessageAsSent(messageId: String) {
        database.messageDao().markAsSent(messageId)
    }

    fun gerProfile(id: String): Flow<UserEntity?> {
        return database.userDao().getProfile(id)
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    suspend fun getProfileOnce(id: String): UserEntity? {
        return database.userDao().getProfileOnce(id)
    }

    suspend fun saveProfile(save: UserEntity) {
        database.userDao().saveUserProfile(save)
    }

    fun gerAllUsers(): Flow<List<UserEntity>>{
        return database.userDao().getAllUsers()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    suspend fun updateLastMessage (userId: String, messageText: String) {
        database.userDao().updateLastMessage(userId, messageText)
    }

}
