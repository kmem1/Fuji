package com.clownteam.profile_domain

data class ProfileData(
    val username: String,
    val avatarUrl: String?,
    val backgroundImageUrl: String?,
    val coursesCount: Int = 2,
    val subscribersCount: Int = 2,
    val subscriptionsCount: Int = 2,
    val profileCourses: List<ProfileCourse> = emptyList(),
    val lastActivityList: List<ProfileActivityDay> = emptyList()
)