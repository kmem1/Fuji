package com.clownteam.ui_profile.change_password

import com.clownteam.components.UiText

data class ChangePasswordScreenState(
    val currentPassword: String = "",
    val currentPasswordError: UiText? = null,
    val newPassword: String = "",
    val newPasswordError: UiText? = null,
    val isLoading: Boolean = false,
    val message: UiText? = null
)