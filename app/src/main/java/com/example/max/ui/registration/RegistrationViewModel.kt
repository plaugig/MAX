package com.example.max.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.data.UserData
import com.example.max.domain.use.cases.SaveProfileUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
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
    private val saveProfileUseCase: SaveProfileUseCase
) : ViewModel() {

    private val _event = MutableSharedFlow<RegistrationEvent>()
    val event: SharedFlow<RegistrationEvent> = _event.asSharedFlow()

    private val _state = MutableStateFlow(RegistrationUiState())
    val state: Flow<RegistrationUiState> get() = _state


    fun signIn(name: String) = viewModelScope.launch(Dispatchers.IO) {
        _state.update { state ->
            state.copy(
                isSingUpButtonLoading = true
            )
        }

        val trimmedName = name.trim()

        if (trimmedName.isEmpty()) {
            _event.emit(RegistrationEvent.EmptyName)
            _state.update { state ->
                state.copy(
                    isSingUpButtonLoading = false
                )
            }

            return@launch
        }

        Firebase.auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = Firebase.auth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = trimmedName
                }

                user?.updateProfile(
                    profileUpdates
                )?.addOnCompleteListener { profileTask ->
                    if (profileTask.isSuccessful) {
                        updateProfile(
                            userId = user.uid,
                            name = trimmedName
                        )
                    }
                }
            } else {
                _state.update { state ->
                    state.copy(
                        isSingUpButtonLoading = false
                    )
                }
                viewModelScope.launch {
                    _event.emit(
                        RegistrationEvent.Error(
                            message = "Error: ${task.exception?.message}"
                        )
                    )
                }
            }
        }
    }

    private fun updateProfile(userId: String, name: String) = viewModelScope.launch(Dispatchers.IO) {
        val userData = UserData(
            userId = userId,
            name = name,
            avatarUrl = null
        )

        try {
            saveProfileUseCase.invoke(userData)
            _event.emit(
                RegistrationEvent.CompleteRegistration
            )
        } catch (e: Exception) {
            _state.update { state ->
                state.copy(
                    isSingUpButtonLoading = false
                )
            }
            _event.emit(
                RegistrationEvent.Error(
                    message = "DB error: ${e.message}"
                )
            )
        }
    }
}
