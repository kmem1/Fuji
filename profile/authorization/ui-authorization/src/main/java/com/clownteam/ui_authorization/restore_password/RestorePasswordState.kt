package com.clownteam.ui_authorization.restore_password

import com.clownteam.components.UiText

data class RestorePasswordState(
    val email: String = "",
    val emailError: UiText? = null,
    val isSuccess: Boolean = false
)
