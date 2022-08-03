package com.clownteam.ui_authorization.restore_password

sealed class RestorePasswordEvent {

    class EmailChanged(val email: String): RestorePasswordEvent()

    object Submit: RestorePasswordEvent()

    object FailedMessageShown: RestorePasswordEvent()

    object NetworkErrorMessageShown: RestorePasswordEvent()
}