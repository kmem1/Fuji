package com.clownteam.collection_domain


import com.clownteam.course_domain.Course

data class CourseCollection(
    val membersAmount: Int,
    val author: String,
    val courses: List<Course>,
    val imageUrl: String,
    val isAdded: Boolean,
    val path: String,
    val rating: Double,
    val title: String
)