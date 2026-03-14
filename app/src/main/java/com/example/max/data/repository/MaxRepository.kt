package com.example.max.data.repository

import com.example.max.data.database.preferences.UserPrefs
import com.example.max.data.MessageData
import com.example.max.data.UserData
import com.example.max.data.source.LocalMaxDataSource
import com.example.max.data.remote.RemoteMaxDataSource
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import androidx.core.net.toUri
import com.example.max.data.mappers.toDomain
import com.example.max.data.mappers.toDomainList
import com.example.max.data.mappers.toDomainListFromFirebase
import com.example.max.data.mappers.toEntity
import com.example.max.data.mappers.toFirebase
import com.example.max.data.mappers.toFirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun getAllRemoteUsers(): Flow<List<UserData>>{
        return remoteDataSource.getAllUsersFromFirebase().map { userFirebases ->
            userFirebases.toDomainListFromFirebase()
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

        val firebase = userData.toFirebaseUser()
        remoteDataSource.saveUserToFirebase(firebase)
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







