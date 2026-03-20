package com.example.max.data.repository

import androidx.core.net.toUri
import com.example.max.data.MessageData
import com.example.max.data.UserData
import com.example.max.data.database.preferences.UserPrefs
import com.example.max.data.mappers.toDomain
import com.example.max.data.mappers.toDomainListFromFirebase
import com.example.max.data.mappers.toEntity
import com.example.max.data.mappers.toFirebase
import com.example.max.data.mappers.toFirebaseUser
import com.example.max.data.remote.RemoteMaxDataSource
import com.example.max.data.remote.UserChatFirebase
import com.example.max.data.source.LocalMaxDataSource
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MaxRepository @Inject constructor(
    private val localDataSource: LocalMaxDataSource,
    private val remoteDataSource: RemoteMaxDataSource,
    private val userPrefs: UserPrefs
) {
    private val storage = FirebaseStorage.getInstance().reference

    fun getMessage(chatId: String, myUid: String): Flow<List<MessageData>> {
        startObservingMessages(chatId)

        return localDataSource.getMessages(chatId).map { entities ->
            entities.map { it.toDomain(myUid) }
        }
    }

    fun getProfile(id: String): Flow<UserData?> {
        return localDataSource.gerProfile(id).map { entity ->
            entity?.toDomain()
        }
    }

    fun getChats(): Flow<List<UserData>> {
       return userPrefs.getMyID().flatMapLatest{ currentUserId ->
            if (currentUserId.isBlank() || currentUserId == "default"){
                flowOf(emptyList())
            } else {
                remoteDataSource.observeUserChats(currentUserId).map { userChats ->
                    userChats
                        .sortedByDescending(UserChatFirebase::lastTimestamp)
                        .map { chat ->
                            UserData(
                                userId = chat.peerUserId,
                                name = chat.peerName,
                                avatarUrl = chat.peerAvatarUrl,
                                lastMessage = chat.lastMessage,
                                threadId = chat.threadId
                            ).also { userData ->
                                cacheContact(userData)
                            }
                        }
                }
            }
        }
    }

    fun getAllRemoteUsers(): Flow<List<UserData>> {
        return remoteDataSource.getAllUsersFromFirebase().map { userFirebases ->
            userFirebases.toDomainListFromFirebase()
        }
    }

    suspend fun sendMessage(message: MessageData, chatId: String, peerUserId: String) {
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

        val senderProfile = localDataSource.getProfileOnce(message.senderId)
        val recipientProfile = localDataSource.getProfileOnce(peerUserId)
        val previewText = message.text.ifBlank { "Photo" }
        val timestamp = messageFirebase.time

        val senderChat = UserChatFirebase(
            threadId = chatId,
            peerUserId = peerUserId,
            peerName = recipientProfile?.name.orEmpty(),
            peerAvatarUrl = recipientProfile?.avatarUrl.orEmpty(),
            lastMessage = previewText,
            lastTimestamp = timestamp
        )
        val recipientChat = UserChatFirebase(
            threadId = chatId,
            peerUserId = message.senderId,
            peerName = senderProfile?.name.orEmpty(),
            peerAvatarUrl = senderProfile?.avatarUrl.orEmpty(),
            lastMessage = previewText,
            lastTimestamp = timestamp
        )

        remoteDataSource.sendMessage(
            message = messageFirebase,
            threadId = chatId,
            senderId = message.senderId,
            recipientId = peerUserId,
            senderChat = senderChat,
            recipientChat = recipientChat
        )
        localDataSource.insertMessage(messageFirebase.toEntity(chatId))
        localDataSource.updateLastMessage(peerUserId, previewText)
    }

    suspend fun singUp(email: String, password: String, name: String){
        val firebaseUser = remoteDataSource.singUp(email,password)

        firebaseUser?.let { user ->
            val userData = UserData(
                userId = user.uid,
                name = name,
                avatarUrl = null
            )
            remoteDataSource.saveUserToFirebase(userData.toFirebaseUser())
            localDataSource.saveProfile(userData.toEntity())
            userPrefs.saveMyId(user.uid)
        }
    }

    suspend fun singIn(email: String, password: String){
        val firebaseUser = remoteDataSource.singIn(email,password)

        firebaseUser?.let { user ->
            val remoteProfile = remoteDataSource.downloadProfile(user.uid)

            remoteProfile?.let { profile ->
                localDataSource.saveProfile(profile.toDomain().toEntity())
            }
            userPrefs.saveMyId(user.uid)
        }
    }



    suspend fun cacheContact(userData: UserData) {
        localDataSource.saveProfile(userData.toEntity())
    }

    fun getCurrentUserId(): Flow<String> {
        return userPrefs.getMyID()
    }

    fun startObservingMessages(chatId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.observeMessages(chatId).collect { firebasesMessage ->
                val entities = firebasesMessage.map {
                    it.toEntity(chatId)
                }
                localDataSource.syncChats(chatId, entities)
            }
        }
    }

    suspend fun clearAllMessage(){
        localDataSource.clearAllMessages()
    }
}
