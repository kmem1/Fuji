package com.example.ui_coursedetailed.domain

import com.clownteam.course_domain.CourseInfo
import com.clownteam.course_domain.ForWhomCourseDescriptionItem
import com.clownteam.course_domain.ModuleItem
import com.clownteam.course_domain.ReviewItem

data class CourseInfoUI(
    val goalDescription: String,
    val forWhomCourseDescriptionItems: List<ForWhomCourseDescriptionItemUI>,
    val learningSkillsDescriptionItems: List<String>,
    val moduleItems: List<ModuleItemUI>,
    val starsPercentage: Map<Int, Int>,
    val reviewItems: List<ReviewItemUI>
)

data class ForWhomCourseDescriptionItemUI(
    val title: String,
    val description: String
)

data class ModuleItemUI(
    val title: String,
    val steps: List<String>
)

data class ReviewItemUI(
    val avatarUrl: String,
    val userName: String,
    val courseRating: Int,
    val dateString: String,
    val content: String
)

internal fun ForWhomCourseDescriptionItem.toUIModel(): ForWhomCourseDescriptionItemUI =
    ForWhomCourseDescriptionItemUI(
        title = this.title,
        description = this.description
    )


internal fun ModuleItem.toUIModel(): ModuleItemUI =
    ModuleItemUI(
        title = this.title,
        steps = this.steps
    )


internal fun ReviewItem.toUiModel(): ReviewItemUI =
    ReviewItemUI(
        avatarUrl = this.avatarUrl,
        userName = this.userName,
        courseRating = this.courseRating,
        dateString = this.dateString,
        content = this.content
    )

internal fun CourseInfo.toUIModel(): CourseInfoUI =
    CourseInfoUI(
        goalDescription = this.goalDescription,
        forWhomCourseDescriptionItems = this.forWhomCourseDescriptionItems.map { it.toUIModel() },
        learningSkillsDescriptionItems = this.learningSkillsDescriptionItems,
        moduleItems = this.moduleItems.map { it.toUIModel() },
        starsPercentage = this.starsPercentage,
        reviewItems = this.reviewItems.map { it.toUiModel() }
    )
