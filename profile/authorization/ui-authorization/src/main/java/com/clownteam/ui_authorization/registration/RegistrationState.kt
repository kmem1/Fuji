package com.clownteam.ui_authorization.registration

import com.clownteam.components.UiText

data class RegistrationState(
    val login: String = "",
    val loginError: UiText? = null,
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: UiText? = null
)