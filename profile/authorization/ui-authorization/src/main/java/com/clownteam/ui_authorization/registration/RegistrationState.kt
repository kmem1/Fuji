package com.clownteam.ui_authorization.registration

import com.clownteam.components.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class RegistrationState(
    val isLoading: Boolean = false,
    val isSuccessRegistration: Boolean = false,
    val message: UiText? = null,
    val login: Flow<String> = emptyFlow(),
    val loginError: UiText? = null,
    val email: Flow<String> = emptyFlow(),
    val emailError: UiText? = null,
    val password: Flow<String> = emptyFlow(),
    val passwordError: UiText? = null,
    val repeatedPassword: Flow<String> = emptyFlow(),
    val repeatedPasswordError: UiText? = null
)