package com.clownteam.ui_profile

import com.clownteam.profile_domain.ProfileData

data class ProfileState(
    val isLoading: Boolean = false,
    val profileData: ProfileData? = ProfileData("", null),
    val isNetworkError: Boolean = false
)