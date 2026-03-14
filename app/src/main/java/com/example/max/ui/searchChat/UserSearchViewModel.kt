package com.example.max.ui.searchChat


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.data.UserData
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
class UserSearchViewModel @Inject constructor(
    private val interactor: MainInteractor
): ViewModel() {

    val users: StateFlow<List<ItemUserData>> = interactor.getAllRemoteUsers()
        .map { userDataList ->
            userDataList.map { user ->
                ItemUserData(
                    id = user.userId,
                    name = user.name,
                    avatarUrl = user.avatarUrl,
                    lastMessage = null
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun createChat(item: ItemUserData){
        viewModelScope.launch {
            val userData = UserData(
                userId = item.id,
                name = item.name,
                avatarUrl = item.avatarUrl,
                lastMessage = null,

            )
            interactor.saveProfile(userData)
        }
    }
}