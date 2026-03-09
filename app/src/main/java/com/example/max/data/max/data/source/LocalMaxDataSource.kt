package com.example.max.data.max.data.source

import android.os.Message
import androidx.room.Database
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
    fun getMessages(): Flow<List<MessageEntity>> {
        return database.messageDao().getAllMessages()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    suspend fun insertMessage(message: MessageEntity) {
        database.messageDao().insertMessage(message)
    }

    suspend fun markMessageAsSent(messageId: String) {
        database.messageDao().markAsSent(messageId)
    }

    fun gerMyProfile(): Flow<UserEntity?> {
        return database.userDao().getMyProfile()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    suspend fun saveProfile(save: UserEntity) {
        database.userDao().saveUserProfile(save)
    }

    fun gerAllUsers(): Flow<List<UserEntity>>{
        return database.userDao().getAllUsers()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }


}