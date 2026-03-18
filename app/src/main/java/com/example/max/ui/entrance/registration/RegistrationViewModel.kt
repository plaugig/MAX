package com.example.max.ui.entrance.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.domain.MainInteractor
import com.example.max.ui.entrance.registration.RegistrationEvent
import com.example.max.ui.entrance.registration.RegistrationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val mainInteractor: MainInteractor
) : ViewModel() {

    private val _event = MutableSharedFlow<RegistrationEvent>()
    val event: SharedFlow<RegistrationEvent> = _event.asSharedFlow()

    private val _state = MutableStateFlow(RegistrationUiState())
    val state: Flow<RegistrationUiState> get() = _state


    fun signUp(name: String, email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        _state.update { state ->
            state.copy(
                isSingUpButtonLoading = true
            )
        }

            val trimmedName = name.trim()
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedName.isEmpty()|| trimmedEmail.isEmpty()|| trimmedPassword.isEmpty()) {
            _event.emit(RegistrationEvent.EmptyFields)
            _state.update { state ->
                state.copy(
                    isSingUpButtonLoading = false
                )
            }

            return@launch
        }

        try {
            mainInteractor.singUp(trimmedEmail, trimmedPassword,trimmedName)

            _event.emit(RegistrationEvent.CompleteRegistration)
        } catch (e: Exception){
            _event.emit(RegistrationEvent.Error(e.message ?: "ну пиздец, ты ЛОХ"))
            _state.update { state ->
                state.copy(
                    isSingUpButtonLoading = false
                )
            }
        }
    }

}