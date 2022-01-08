package com.clownteam.course_datasource.cache.model

import com.clownteam.course_domain.Course

data class CourseEntity(
    val id: String,
    val title: String,
    val imgUrl: String,
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

internal fun CourseEntity.toCourse(): Course =
    Course(
        id = id,
        title = title,
        imgUrl = imgUrl,
        description = description,
        price = price,
        durationInMinutes = durationInMinutes,
        rating = rating,
        marksCount = marksCount,
        membersAmount = membersAmount,
        hasCertificate = hasCertificate,
        maxProgressPoints = maxProgressPoints,
        currentPoints = currentPoints,
        authorName = authorName
    )