package com.clownteam.ui_coursepassing.course_steps

import com.clownteam.course_domain.CourseStep

sealed class CourseStepsState {

    object Loading : CourseStepsState()

    data class Data(
        val lessonTitle: String,
        val steps: List<CourseStep>,
        val currentStepIndex: Int,
        val currentStepContent: String
    ) : CourseStepsState()

    data class CurrentStepLoading(
        val lessonTitle: String,
        val steps: List<CourseStep>,
        val currentStepIndex: Int,
    ) : CourseStepsState()

    object Error : CourseStepsState()

    object NetworkError : CourseStepsState()

    object Unauthorized : CourseStepsState()
}