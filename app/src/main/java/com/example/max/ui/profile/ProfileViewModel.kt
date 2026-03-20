package com.example.max.ui.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.domain.MainInteractor
import com.example.max.ui.common.ItemUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: MainInteractor,
    savedStateHandle: SavedStateHandle
): ViewModel() {



private val peerUserId: String? = savedStateHandle.get<String>("userId")

    val profile: StateFlow<ItemUserData?> = flow<ItemUserData?> {
        if (peerUserId != null){
            emitAll(interactor.getChatProfile(peerUserId))
        } else {
            emitAll(interactor.getMyProfile())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )


    suspend fun clearAllMessages(){
        interactor.clearAllMessages()
    }
}