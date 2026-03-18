package com.example.max.ui.entrance.autorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.domain.MainInteractor
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
class AuthorizationViewModel @Inject constructor(
    private val interactor: MainInteractor
): ViewModel(){
    private val _event = MutableSharedFlow<AuthorizationEvent>()
    val event: SharedFlow<AuthorizationEvent> = _event.asSharedFlow()

    private val _state = MutableStateFlow(AuthorizationUiState())
    val state: Flow<AuthorizationUiState> get() = _state

    fun singIn(email: String, password: String) = viewModelScope.launch(Dispatchers.IO){
        _state.update { state ->
            state.copy(
                isSingUpButtonLoading = true
            )
        }

        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isEmpty()|| trimmedPassword.isEmpty()){
            _event.emit(AuthorizationEvent.EmptyFields)
            _state.update { state ->
                state.copy(
                    isSingUpButtonLoading = false
                )
            }
            return@launch
        }

        try {
            interactor.singIn(trimmedEmail,trimmedPassword)

            _event.emit(AuthorizationEvent.CompleteAuthorization)
        } catch (e: Exception){
            _event.emit(AuthorizationEvent.Error(e.message ?: "error"))
            _state.update { state ->
                state.copy(
                    isSingUpButtonLoading = false
                )
            }
        }
    }
}