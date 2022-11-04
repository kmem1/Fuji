package com.clownteam.ui_profile.change_profile

import com.clownteam.components.UiText

data class ChangeProfileScreenState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val avatarUrl: String? = null,
    val username: String = "",
    val usernameError: UiText? = null,
    val message: UiText? = null,
    val isUnauthorized: Boolean = false
)