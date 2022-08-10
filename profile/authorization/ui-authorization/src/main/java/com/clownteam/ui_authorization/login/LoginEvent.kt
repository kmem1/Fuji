package com.clownteam.ui_authorization.login

sealed class LoginEvent {

    class EmailChanged(val email: String): LoginEvent()

    class PasswordChanged(val password: String): LoginEvent()

    object FailMessageShown: LoginEvent()

    object Submit: LoginEvent()
}