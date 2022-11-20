package com.clownteam.ui_profile.change_password

sealed class ChangePasswordScreenEvent {

    class SetCurrentPassword(val password: String) : ChangePasswordScreenEvent()
    class SetNewPassword(val password: String) : ChangePasswordScreenEvent()
    object ApplyChanges : ChangePasswordScreenEvent()
    object MessageShown : ChangePasswordScreenEvent()
}