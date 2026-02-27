package com.example.max.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.max.data.database.entities.MessageEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(massage: MessageEntity)

    @Query("UPDATE messages SET isSent = 1 WHERE id = :messageId")
    suspend fun markAsSent(messageId: String)
}