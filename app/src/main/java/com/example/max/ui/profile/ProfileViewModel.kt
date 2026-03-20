package com.example.max.ui.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.domain.MainInteractor
import com.example.max.ui.common.ItemUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: MainInteractor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val peerUserId: String? = savedStateHandle.get<String>("userId")

    private val _state = MutableStateFlow(ProfileUiState())
    val state: Flow<ProfileUiState> get() = _state


    init {
        sync()
    }

    private fun sync() = viewModelScope.launch(Dispatchers.IO) {
        val flow = if (peerUserId != null) {
            interactor.getChatProfile(peerUserId)
        } else {
            interactor.getMyProfile()
        }

        flow.collect { profile ->
            _state.update { state ->
                state.copy(
                    profile = profile,
                    isCurrentProfile = peerUserId.isNullOrBlank()
                )
            }
        }
    }


    fun clearAllMessages() = viewModelScope.launch(Dispatchers.IO) {
        interactor.clearAllMessages()
    }
}