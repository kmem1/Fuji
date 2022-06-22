package com.clownteam.ui_coursedetailed.ui

import com.clownteam.course_domain.Course
import com.clownteam.course_domain.CourseInfoUI

sealed class CourseDetailedState {

    object Loading : CourseDetailedState()

    data class Data(val course: Course, val courseInfo: CourseInfoUI) : CourseDetailedState()

    object Error : CourseDetailedState()

    object Unauthorized : CourseDetailedState()

}