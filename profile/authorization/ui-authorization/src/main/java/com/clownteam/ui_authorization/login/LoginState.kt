package com.clownteam.ui_authorization.login

import com.clownteam.components.UiText

data class LoginState(
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isNetworkError: Boolean = false,
    val isLoading: Boolean = false
)