package com.example.max.data.max.repository

import com.example.max.data.database.entities.MessageEntity
import com.example.max.data.max.MessageData
import com.example.max.data.max.UserData
import com.example.max.data.max.data.source.LocalMaxDataSource
import com.example.max.data.remote.data.MessageFirebase
import com.example.max.data.remote.data.RemoteMaxDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class MaxRepository @Inject constructor(
    private val localDataSource: LocalMaxDataSource,
    private val remoteDataSource: RemoteMaxDataSource
){
    fun getMessage (myUid: String): Flow<List<MessageData>>{
        return localDataSource.getMessages().map { entities ->
            entities.map { it.toDomain(myUid)
            }
        }
    }

    fun getMyProfile(): Flow<UserData?>{
        return localDataSource.gerMyProfile().map { entity ->
            entity?.toDomain()
        }
    }

    suspend fun sendTestMessage() {
        val testMessage = MessageFirebase(
            id = "test_id_${System.currentTimeMillis()}",
            text = "Ебать, оно работает!",
            senderId = "user_1",
            time = System.currentTimeMillis()
        )
        remoteDataSource.sendMessage(testMessage)
    }

}







