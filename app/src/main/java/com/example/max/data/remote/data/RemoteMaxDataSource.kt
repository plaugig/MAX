package com.example.max.data.remote.data

import android.icu.util.ValueIterator
import android.os.Message
import android.view.inputmethod.TextSnapshot
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

    private val messageRef = database.getReference("messages")

    suspend fun sendMessage(message: MessageFirebase){
        messageRef.child(message.id).setValue(message).await()
    }

    fun observeMessages(): Flow<List<MessageFirebase>> = callbackFlow {
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
        messageRef.addValueEventListener(listener)
        awaitClose {
            messageRef.removeEventListener(listener)
        }
    }
}