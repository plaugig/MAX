package com.example.max.ui.entrance.registration

sealed interface RegistrationEvent {
    data object EmptyFields : RegistrationEvent

    data class Error(
        val message: String
    ) : RegistrationEvent

    data object CompleteRegistration : RegistrationEvent

}