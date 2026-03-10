package com.example.max.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.domain.MainInteractor
import com.example.max.ui.chat.item.ItemMessageData
import com.example.max.ui.item.ItemUserData
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
    private val interactor: MainInteractor
) : ViewModel(){

    private val currentUserId = "my_test_uid"

    private val _chatId = MutableStateFlow<String?>(null)

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val message: StateFlow<List<ItemMessageData>> = _chatId
        .filterNotNull()
        .flatMapLatest { id ->
            interactor.getMessage(id)
        }
        .map { domainList ->
            domainList.map { message ->
                ItemMessageData(
                    id = message.id,
                    text = message.text,
                    time = formatTime(message.time.toLongOrNull() ?: 0L),
                    isMine = message.senderId == currentUserId ,
                    isSent = message.isSent,
                    imageUrl = message.imageUrl
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val chatUser: StateFlow<ItemUserData?> = _chatId
        .filterNotNull()
        .flatMapLatest { id ->
            interactor.get
        }


    fun setupChat(id: String){
        _chatId.value = id
    }

    fun sendMessage (text: String){
        val currentId = _chatId.value ?: return
        viewModelScope.launch {
            interactor.sendMessage(text, currentId)
        }
    }

   private fun formatTime(millis: Long): String {
        val sdf = SimpleDateFormat("HH.mm", Locale.getDefault())
        return sdf.format(Date(millis))
    }
}