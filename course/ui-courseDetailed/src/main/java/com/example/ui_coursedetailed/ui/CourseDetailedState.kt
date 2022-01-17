package com.example.ui_coursedetailed.ui

import com.clownteam.core.domain.ProgressBarState
import com.clownteam.course_domain.Course
import com.example.ui_coursedetailed.domain.CourseInfoUI

data class CourseDetailedState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val course: Course? = null,
    val courseInfo: CourseInfoUI? = null,
    val isError: Boolean = false
) {
    val hasAllData: Boolean
        get() = course != null && courseInfo != null
}