package com.example.max.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.domain.MainInteractor
import com.example.max.ui.common.ItemMessageData
import com.example.max.ui.common.ItemUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: MainInteractor
) : ViewModel() {

    private val chatId = savedStateHandle.get<String>("chatId")!!

    private val currentUserId = "my_test_uid"


    val message: StateFlow<List<ItemMessageData>> = interactor.getMessage(
        chatId = chatId,
        myUid = currentUserId
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val chatUser: StateFlow<ItemUserData?> = interactor.getChatProfile(
        id = chatId
    ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


    fun sendMessage(text: String) {
        val currentId = chatId
        viewModelScope.launch {
            interactor.sendMessage(text, currentId)
        }
    }

    fun sendImageMessage(uri: String) {
        val currentChatId = chatId

        viewModelScope.launch {
            interactor.sendMessage(
                text = null,
                imageUrl = uri,
                chatId = currentChatId
            )
        }
    }
}