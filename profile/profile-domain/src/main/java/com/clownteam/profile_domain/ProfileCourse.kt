package com.clownteam.profile_domain

data class ProfileCourse(
    val courseId: String,
    val title: String,
    val authorName: String,
    val rating: Float,
    val marksAmount: Int,
    val price: Int,
    val imageUrl: String,
    val membersAmount: Int
)