package com.clownteam.ui_coursedetailed.ui

import com.clownteam.course_domain.Course
import com.clownteam.course_domain.CourseInfoUI

sealed class CourseDetailedState {

    object Loading : CourseDetailedState()

    /**
     * @param shouldStartToLearn Determines if UI should navigate to learning course or not
     */
    data class Data(
        val course: Course,
        val courseInfo: CourseInfoUI,
        var shouldStartToLearn: Boolean = false
    ) : CourseDetailedState()

    object Error : CourseDetailedState()

    object Unauthorized : CourseDetailedState()

}