package com.clownteam.course_domain

import java.util.*

data class Course(
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
    val authorName: String,
    val id: String = UUID.randomUUID().toString(),
)