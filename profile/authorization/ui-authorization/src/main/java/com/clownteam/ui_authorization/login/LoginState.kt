package com.clownteam.ui_authorization.login

import com.clownteam.components.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class LoginState(
    val email: Flow<String> = emptyFlow(),
    val emailError: UiText? = null,
    val password: Flow<String> = emptyFlow(),
    val passwordError: UiText? = null,
    val isNetworkError: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: UiText? = null
)
