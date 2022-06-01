package com.clownteam.ui_profile

sealed class ProfileEvent {

    class GetProfile(val accessToken: String? = null) : ProfileEvent()

    object SignOut : ProfileEvent()
}