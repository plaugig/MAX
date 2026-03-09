package com.example.max.data.max.repository

import com.example.max.data.max.MessageData
import com.example.max.data.max.UserData
import com.example.max.data.max.data.source.LocalMaxDataSource
import com.example.max.data.remote.data.MessageFirebase
import com.example.max.data.remote.data.RemoteMaxDataSource
import com.example.max.data.remote.data.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
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

    fun getAllUser(): Flow<List<UserData>> {
        return localDataSource.gerAllUsers().map { entities ->
            entities.toDomain()
        }
    }

    suspend fun sendMessage(text: String, myUid: String){
        val messageFirebase = MessageFirebase(
            id = UUID.randomUUID().toString(),
            senderId = myUid,
            text = text,
            time = System.currentTimeMillis()
        )

        remoteDataSource.sendMessage(messageFirebase)

        localDataSource.insertMessage(messageFirebase.toEntity())
    }

    suspend fun saveProfile(userData: UserData){
        val entity = userData.toEntity()
        localDataSource.saveProfile(entity)
    }

}







