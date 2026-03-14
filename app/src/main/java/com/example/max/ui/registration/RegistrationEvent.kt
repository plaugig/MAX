package com.example.max.ui.registration

sealed interface RegistrationEvent {
    data object EmptyName : RegistrationEvent

    data class Error(
        val message: String
    ) : RegistrationEvent

    data object CompleteRegistration : RegistrationEvent
}