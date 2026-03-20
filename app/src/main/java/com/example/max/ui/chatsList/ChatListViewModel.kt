package com.example.max.ui.chatsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.domain.MainInteractor
import com.example.max.ui.common.ItemUserData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val interactor: MainInteractor
) : ViewModel(), ChatListUiActionListener {

    private val eventChannel = Channel<ChatListUiEvent>()
    val event: Flow<ChatListUiEvent> = eventChannel.receiveAsFlow()

    val chats: StateFlow<List<ItemUserData>> = interactor.getChats()
        .map { userDataList ->
            userDataList.map { user ->
                ItemUserData(
                    id = user.userId,
                    name = user.name,
                    avatarUrl = user.avatarUrl,
                    lastMessage = user.lastMessage ?: "No messages yet",
                    threadId = user.threadId
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    init {
        checkUserAuth()
    }

    private fun checkUserAuth() = viewModelScope.launch(Dispatchers.IO) {
        if (Firebase.auth.currentUser == null) {
            eventChannel.send(
                ChatListUiEvent.NavigateToRegistration
            )
        }
    }

    override fun openChat(threadId: String, peerUserId: String) {
        viewModelScope.launch {
            eventChannel.send(
                ChatListUiEvent.OpenChat(
                    threadId = threadId,
                    peerUserId = peerUserId
                )
            )
        }
    }
}
