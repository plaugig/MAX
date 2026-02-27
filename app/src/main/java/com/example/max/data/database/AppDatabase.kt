package com.example.max.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.max.data.database.dao.MessageDao
import com.example.max.data.database.dao.UserDao
import com.example.max.data.database.entities.MessageEntity
import com.example.max.data.database.entities.UserEntity


@Database(
    entities = [UserEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
}