package com.clownteam.ui_profile.change_profile

sealed class ChangeProfileScreenEvent {

    class SetUsername(val username: String) : ChangeProfileScreenEvent()
    object GetProfileData : ChangeProfileScreenEvent()
    object ApplyChanges : ChangeProfileScreenEvent()
    object MessageShown : ChangeProfileScreenEvent()
}