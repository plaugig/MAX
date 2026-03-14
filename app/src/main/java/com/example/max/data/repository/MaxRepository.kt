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
        val currentUserId = userPrefs.getMyID()
        if (currentUserId.isBlank()) return flowOf(emptyList())

        return remoteDataSource.observeUserChats(currentUserId).map { userChats ->
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

    suspend fun saveProfile(userData: UserData) {
        val entity = userData.toEntity()
        localDataSource.saveProfile(entity)

        val firebase = userData.toFirebaseUser()
        remoteDataSource.saveUserToFirebase(firebase)
    }

    suspend fun cacheContact(userData: UserData) {
        localDataSource.saveProfile(userData.toEntity())
    }

    fun getCurrentUserIdFromPrefs(): String {
        return userPrefs.getMyID()
    }

    fun startObservingMessages(chatId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.observeMessages(chatId).collect { firebasesMessage ->
                firebasesMessage.forEach { msg ->
                    localDataSource.insertMessage(msg.toEntity(chatId))
                }
            }
        }
    }
}
