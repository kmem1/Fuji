package com.clownteam.ui_coursepassing.course_modules

import com.clownteam.course_domain.Course
import com.clownteam.course_domain.CourseModule

sealed class CourseModulesState {

    object Loading : CourseModulesState()

    class Data(val course: Course?, val modules: List<CourseModule>, val courseId: String?) : CourseModulesState()

    object Error : CourseModulesState()

    object NetworkError : CourseModulesState()

    object Unauthorized : CourseModulesState()
}