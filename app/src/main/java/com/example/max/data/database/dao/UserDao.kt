package com.example.max.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.max.data.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(user: UserEntity)

    @Query("SELECT * FROM user_profile WHERE userId = :id")
    fun getProfile(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM user_profile ")
    fun getAllUsers(): Flow<List<UserEntity>>
}