package com.example.max.data.remote

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
    private val database: FirebaseDatabase
) {

    suspend fun sendMessage(message: MessageFirebase, chatId: String){
        database.getReference("chats")
            .child(chatId)
            .child("messages")
            .child(message.id)
            .setValue(message)
            .await()
    }

    fun observeMessages(chatId: String): Flow<List<MessageFirebase>> = callbackFlow {

        val chatMessageRef = database.getReference("chats")
            .child(chatId)
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

    fun getAllUsersFromFirebase(): Flow<List<UserFirebase>> = callbackFlow {
        val userRef = database.getReference("users")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull {
                    it.getValue(UserFirebase :: class.java)
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

    suspend fun saveUserToFirebase(user: UserFirebase){
        database.getReference("users")
            .child(user.userId)
            .setValue(user)
            .await()
    }
}