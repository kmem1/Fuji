package com.clownteam.ui_profile

data class ProfileState(
    val isLoading: Boolean = false,
    val username: String = "",
    val avatarUrl: String? = null,
    val isNetworkError: Boolean = false
)