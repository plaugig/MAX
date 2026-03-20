package com.example.max.ui.profile

import com.example.max.ui.common.ItemUserData

data class ProfileUiState(
    val profile: ItemUserData? = null,
    val isCurrentProfile: Boolean = false
)