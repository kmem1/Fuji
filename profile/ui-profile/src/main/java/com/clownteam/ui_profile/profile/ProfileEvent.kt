package com.clownteam.ui_profile.profile

sealed class ProfileEvent {

    object GetProfile : ProfileEvent()

    object GetProfileCourses: ProfileEvent()

    object GetProfileCollections: ProfileEvent()

    object SignOut : ProfileEvent()
}