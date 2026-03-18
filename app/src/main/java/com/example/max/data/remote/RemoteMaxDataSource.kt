package com.example.max.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteMaxDataSource @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) {

    suspend fun sendMessage(
        message: MessageFirebase,
        threadId: String,
        senderId: String,
        recipientId: String,
        senderChat: UserChatFirebase,
        recipientChat: UserChatFirebase
    ) {
        val chatRef = database.getReference("chats").child(threadId)

        chatRef.child("participants").child(senderId).setValue(true).await()
        chatRef.child("participants").child(recipientId).setValue(true).await()
        chatRef.child("lastMessage").setValue(senderChat.lastMessage).await()
        chatRef.child("lastTimestamp").setValue(senderChat.lastTimestamp).await()
        chatRef.child("messages").child(message.id).setValue(message).await()

        database.getReference("userChats")
            .child(senderId)
            .child(threadId)
            .setValue(senderChat)
            .await()

        database.getReference("userChats")
            .child(recipientId)
            .child(threadId)
            .setValue(recipientChat)
            .await()
    }

    fun observeMessages(threadId: String): Flow<List<MessageFirebase>> = callbackFlow {
        val chatMessageRef = database.getReference("chats")
            .child(threadId)
            .child("messages")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull {
                    it.getValue(MessageFirebase::class.java)
                }
                trySend(items)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        chatMessageRef.addValueEventListener(listener)
        awaitClose {
            chatMessageRef.removeEventListener(listener)
        }
    }

    fun observeUserChats(userId: String): Flow<List<UserChatFirebase>> = callbackFlow {
        val userChatsRef = database.getReference("userChats")
            .child(userId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull {
                    it.getValue(UserChatFirebase::class.java)
                }
                trySend(items)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        userChatsRef.addValueEventListener(listener)
        awaitClose {
            userChatsRef.removeEventListener(listener)
        }
    }

    fun getAllUsersFromFirebase(): Flow<List<UserFirebase>> = callbackFlow {
        val userRef = database.getReference("users")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull {
                    it.getValue(UserFirebase::class.java)
                }
                trySend(users)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        userRef.addValueEventListener(listener)
        awaitClose {
            userRef.removeEventListener(listener)
        }
    }

    suspend fun singUp (email: String, password: String): FirebaseUser? {
        return auth.createUserWithEmailAndPassword(email, password).await().user
    }

    suspend fun singIn(email: String, password: String): FirebaseUser?{
        return auth.signInWithEmailAndPassword(email,password).await().user
    }

    suspend fun saveUserToFirebase (user: UserFirebase){
        database.getReference("users")
            .child(user.userId)
            .setValue(user)
            .await()
    }

    suspend fun downloadProfile(uid: String): UserFirebase?{
        val snapshot = database.getReference("users")
            .child(uid)
            .get()
            .await()

        return snapshot.getValue(UserFirebase :: class.java)
    }



}
