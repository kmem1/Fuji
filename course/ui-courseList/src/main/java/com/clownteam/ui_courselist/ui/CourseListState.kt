package com.clownteam.ui_courselist.ui

import com.clownteam.core.domain.ProgressBarState
import com.clownteam.course_domain.Course

data class CourseListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val courses: List<Course> = emptyList(),
    val isError: Boolean = false
)