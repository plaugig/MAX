package com.example.max.ui.chatsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.max.R
import com.example.max.domain.MainInteractor
import com.example.max.ui.common.ItemUserData
import com.example.max.ui.registration.RegistrationEvent
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val interactor: MainInteractor
): ViewModel(), ChatListUiActionListener {

    private val _event = MutableSharedFlow<ChatListUiEvent>()
    val event: SharedFlow<ChatListUiEvent> = _event.asSharedFlow()

    val chats: StateFlow<List<ItemUserData>> = interactor.getChats()
        .map { userDataList ->
            userDataList.map { user ->
                ItemUserData(
                    id = user.userId,
                    name = user.name,
                    avatarUrl = user.avatarUrl,
                    lastMessage = user.lastMessage ?:"сообщений нет"
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
            _event.emit(
                ChatListUiEvent.NavigateToRegistration
            )
        }
    }

    override fun openChat(chatId: String) {
        viewModelScope.launch {
            _event.emit(
                ChatListUiEvent.OpenChat(
                    chatId = chatId
                )
            )
        }
    }
}