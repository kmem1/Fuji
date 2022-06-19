package com.clownteam.ui_coursepassing.course_lessons

import com.clownteam.course_domain.CourseLesson

sealed class CourseLessonsState {

    object Loading : CourseLessonsState()

    class Data(
        val courseId: String,
        val moduleId: String,
        val moduleName: String,
        val lessons: List<CourseLesson>
    ) :
        CourseLessonsState()

    object Error : CourseLessonsState()

    object NetworkError : CourseLessonsState()

    object Unauthorized : CourseLessonsState()
}