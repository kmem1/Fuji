package com.clownteam.ui_coursepassing.course_lessons

import com.clownteam.course_domain.Course
import com.clownteam.course_domain.CourseLesson
import com.clownteam.course_domain.CourseModule

sealed class CourseLessonsState {

    object Loading : CourseLessonsState()

    class Data(val moduleName: String, val lessons: List<CourseLesson>) : CourseLessonsState()

    object Error : CourseLessonsState()

    object NetworkError : CourseLessonsState()

    object Unauthorized : CourseLessonsState()
}