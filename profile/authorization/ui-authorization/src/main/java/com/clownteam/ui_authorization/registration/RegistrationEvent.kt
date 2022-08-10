package com.clownteam.ui_authorization.registration

sealed class RegistrationEvent {

    class LoginChanged(val login: String): RegistrationEvent()

    class EmailChanged(val email: String): RegistrationEvent()

    class PasswordChanged(val password: String): RegistrationEvent()

    class RepeatedPasswordChanged(val repeatedPassword: String): RegistrationEvent()

    object MessageShown: RegistrationEvent()

    object Submit: RegistrationEvent()
}