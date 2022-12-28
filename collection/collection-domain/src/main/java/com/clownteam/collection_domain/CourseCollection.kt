package com.clownteam.collection_domain


import com.clownteam.course_domain.Course

data class CourseCollection(
    val membersAmount: Int,
    val author: CourseCollectionAuthor,
    val courses: List<Course>,
    val imageUrl: String,
    val isAdded: Boolean,
    val id: String,
    val rating: Double,
    val title: String,
    val description: String,
    val isEditable: Boolean
)

data class CourseCollectionAuthor(
    val id: String,
    val name: String,
    val avatar_url: String
)