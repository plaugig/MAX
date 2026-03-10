package com.example.max.ui.chatsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.data.max.UserData
import com.example.max.domain.MainInteractor
import com.example.max.ui.item.ItemUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val interactor: MainInteractor
): ViewModel() {
    val chats: StateFlow<List<ItemUserData>> = interactor.getChats()
        .map { userDataList ->
            userDataList.map { user ->
                ItemUserData(
                    id = user.userId,
                    name = user.name,
                    avatarUrl = user.avatarUrl,
                    lastMessage = "верни бабки гандон",
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveTestUsers(users: List<UserData>) {
        viewModelScope.launch {
            users.forEach { user ->
                interactor.saveProfile(user)
            }
        }
    }
}