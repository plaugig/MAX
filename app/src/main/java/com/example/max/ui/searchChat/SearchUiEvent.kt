package com.example.max.ui.searchChat

sealed interface SearchUiEvent {
    data class OpenChat(
        val userId: String
    ) : SearchUiEvent
}