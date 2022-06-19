package com.clownteam.course_domain

data class CourseLesson(
    val id: String,
    val title: String,
    val currentProgress: Int,
    val maxProgress: Int,
    val currentStepId: String
)