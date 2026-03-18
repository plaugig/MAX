package com.example.max.ui.searchChat


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.data.UserData
import com.example.max.domain.DirectChatThreadId
import com.example.max.domain.MainInteractor
import com.example.max.ui.common.ItemUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserSearchViewModel @Inject constructor(
    private val interactor: MainInteractor
) : ViewModel(), SearchUiActionListener {

    private val _event = MutableSharedFlow<SearchUiEvent>()
    val event: SharedFlow<SearchUiEvent> = _event.asSharedFlow()
         private val currentUserId = interactor.getCurrentUserId()

    val users: StateFlow<List<ItemUserData>> = interactor.getAllRemoteUsers()
        .combine(currentUserId){ userList, userId ->
            userList.filterNot { it.id == userId }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun openChat(user: ItemUserData) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = currentUserId.first()
            interactor.cacheContact(
                user = UserData(
                    userId = user.id,
                    name = user.name,
                    avatarUrl = user.avatarUrl,
                    lastMessage = null
                )
            )

            val threadId = DirectChatThreadId.from(userId, user.id)

            _event.emit(
                SearchUiEvent.OpenChat(
                    threadId = threadId,
                    peerUserId = user.id
                )
            )
        }
    }
}
