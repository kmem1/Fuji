package com.clownteam.course_domain

data class CourseModule(
    val id: String,
    val title: String,
    val currentProgress: Int,
    val maxProgress: Int
)