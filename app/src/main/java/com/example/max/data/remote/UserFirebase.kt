package com.example.max.data.remote

data class UserFirebase(
    val userId: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val isOnline: Boolean = false
)
