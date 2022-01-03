package com.clownteam.course_domain

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val price: Float,
    val durationInMinutes: Int,
    val rating: Float,
    val marksCount: Int,
    val membersAmount: Int,
    val hasCertificate: Boolean,
    val maxProgressPoints: Int,
    val currentPoints: Int,
    val authorName: String
)