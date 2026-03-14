package com.example.max.ui.chatsList

sealed interface ChatListUiEvent {
    data object NavigateToRegistration : ChatListUiEvent
    data class OpenChat(
        val chatId: String
    ) : ChatListUiEvent
}