package com.clownteam.ui_profile

sealed class ProfileEvent {

    object GetProfile : ProfileEvent()

    object SignOut : ProfileEvent()
}