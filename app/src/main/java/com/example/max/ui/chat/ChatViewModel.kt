package com.example.max.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.domain.MainInteractor
import com.example.max.ui.common.ItemMessageData
import com.example.max.ui.common.ItemUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: MainInteractor
) : ViewModel() {

    private val threadId = savedStateHandle.get<String>("threadId")
        ?: savedStateHandle.get<String>("chatId")
        ?: error("threadId is required")
    private val peerUserId = savedStateHandle.get<String>("peerUserId")
        ?: savedStateHandle.get<String>("chatId")
        ?: error("peerUserId is required")

    private val currentUserId = interactor.getCurrentUserId()

    fun getPeerUserId(): String = peerUserId

    val message: StateFlow<List<ItemMessageData>> = currentUserId.flatMapLatest { userId ->
        interactor.getMessage(
            chatId = threadId,
            myUid = userId
        )
    } .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    val chatUser: StateFlow<ItemUserData?> = interactor.getChatProfile(
        id = peerUserId
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun sendMessage(text: String) {
        viewModelScope.launch {
            interactor.sendMessage(
                text = text,
                chatId = threadId,
                peerUserId = peerUserId
            )
        }
    }

    fun sendImageMessage(uri: String) {
        viewModelScope.launch {
            interactor.sendMessage(
                text = null,
                imageUrl = uri,
                chatId = threadId,
                peerUserId = peerUserId
            )
        }
    }


}
