package com.example.max.ui.searchChat


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.max.data.UserData
import com.example.max.domain.MainInteractor
import com.example.max.ui.common.ItemUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserSearchViewModel @Inject constructor(
    private val interactor: MainInteractor
) : ViewModel(), SearchUiActionListener {

    private val _event = MutableSharedFlow<SearchUiEvent>()
    val event: SharedFlow<SearchUiEvent> = _event.asSharedFlow()

    val users: StateFlow<List<ItemUserData>> = interactor.getAllRemoteUsers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun openChat(user: ItemUserData) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.saveProfile(
                user = UserData(
                    userId = user.id,
                    name = user.name,
                    avatarUrl = user.avatarUrl,
                    lastMessage = null
                )
            )
        }
    }
}