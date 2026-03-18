package com.example.max.ui.entrance.autorization

sealed interface AuthorizationEvent {
    data object EmptyFields : AuthorizationEvent

    data class Error(
        val message: String
    ) : AuthorizationEvent

    data object CompleteAuthorization : AuthorizationEvent
}