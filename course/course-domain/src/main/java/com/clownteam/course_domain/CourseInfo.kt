package com.clownteam.course_domain

data class CourseInfo(
    val courseId: String,
    val goalDescription: String,
    val forWhomCourseDescriptionItems: List<ForWhomCourseDescriptionItem>,
    val learningSkillsDescriptionItems: List<String>,
    val moduleItems: List<ModuleItem>,
    val starsPercentage: Map<Int, Int>,
    val reviewItems: List<ReviewItem>
)

data class ForWhomCourseDescriptionItem(
    val title: String,
    val description: String
)

data class ModuleItem(
    val title: String,
    val steps: List<String>
)

data class ReviewItem(
    val avatarUrl: String,
    val userName: String,
    val courseRating: Int,
    val dateString: String,
    val content: String
)