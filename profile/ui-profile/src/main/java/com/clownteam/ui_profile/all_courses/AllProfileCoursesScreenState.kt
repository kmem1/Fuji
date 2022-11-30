package com.clownteam.ui_profile.all_courses

import com.clownteam.components.UiText
import com.clownteam.profile_domain.ProfileCourse

data class AllProfileCoursesScreenState(
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val isUnauthorized: Boolean = false,
    val courses: List<ProfileCourse> = emptyList()
)
