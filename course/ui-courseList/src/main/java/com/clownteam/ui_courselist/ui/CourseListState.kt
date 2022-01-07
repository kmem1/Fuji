package com.clownteam.ui_courselist.ui

import com.clownteam.core.domain.ProgressBarState
import com.clownteam.course_domain.Course

data class CourseListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val myCourses: List<Course> = emptyList(),
    val popularCourses: List<Course> = emptyList(),
    val isError: Boolean = false
) {
    val hasAllData: Boolean
        get() = myCourses.isNotEmpty() && popularCourses.isNotEmpty()
}
