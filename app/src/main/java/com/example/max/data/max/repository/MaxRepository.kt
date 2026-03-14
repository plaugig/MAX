package com.example.max.data.max.repository

import com.example.max.data.database.preferences.UserPrefs
import com.example.max.data.max.MessageData
import com.example.max.data.max.UserData
import com.example.max.data.max.data.source.LocalMaxDataSource
import com.example.max.data.remote.data.RemoteMaxDataSource
import com.example.max.data.remote.data.toEntity
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class MaxRepository @Inject constructor(
    private val localDataSource: LocalMaxDataSource,
    private val remoteDataSource: RemoteMaxDataSource,
    private val userPrefs: UserPrefs
){
    private val storage = FirebaseStorage.getInstance().reference

    fun getMessage (chatId: String, myUid: String): Flow<List<MessageData>>{

        startObservingMessages(chatId)

        return localDataSource.getMessages(chatId).map { entities ->
            entities.map { it.toDomain(myUid)
            }
        }
    }

    fun getProfile(id: String): Flow<UserData?>{
        return localDataSource.gerProfile(id).map { entity ->
            entity?.toDomain()
        }
    }

    fun getAllUser(): Flow<List<UserData>> {
        return localDataSource.gerAllUsers().map { entities ->
            entities.toDomainList()
        }
    }

    suspend fun sendMessage(message: MessageData, chatId: String){
        var messageFirebase = message.toFirebase()

        if (
            !message.imageUrl.isNullOrBlank() &&
            message.imageUrl.startsWith("content://")
            ) {
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val fileRef = storage.child("chats/$chatId/$fileName")

            fileRef.putFile(message.imageUrl.toUri()).await()

            val downloadUrl = fileRef.downloadUrl.await().toString()

            messageFirebase = messageFirebase.copy(imageUrl = downloadUrl)
        }

        remoteDataSource.sendMessage(messageFirebase, chatId)
        localDataSource.insertMessage(messageFirebase.toEntity(chatId)  )

        localDataSource.updateLastMessage(chatId, message.text)

    }

    suspend fun saveProfile(userData: UserData){
        val entity = userData.toEntity()
        localDataSource.saveProfile(entity)
    }

    fun getCurrentUserIdFromPrefs(): String {
        return userPrefs.getMyID()
    }

    fun startObservingMessages(chatId: String){
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.observeMessages(chatId).collect { firebasesMessage ->
                firebasesMessage.forEach { msg ->
                    localDataSource.insertMessage(msg.toEntity(chatId))

                    localDataSource.updateLastMessage(chatId,msg.text)
                }
            }
        }
    }


}







