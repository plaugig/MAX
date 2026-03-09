package com.example.max.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val userId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "avatarUrl")
    val avatarUrl: String
)
