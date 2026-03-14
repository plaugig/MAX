package com.example.max.ui.searchChat

sealed interface SearchUiEvent {
    data class OpenChat(
        val threadId: String,
        val peerUserId: String
    ) : SearchUiEvent
}
