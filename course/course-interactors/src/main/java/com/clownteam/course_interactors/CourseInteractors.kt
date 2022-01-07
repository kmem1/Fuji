package com.clownteam.course_interactors

import com.clownteam.course_datasource.cache.CourseCache

class CourseInteractors private constructor(
    val getMyCourses: IGetMyCoursesUseCase,
    val getPopularCourses: IGetPopularCoursesUseCase
) {
    companion object Factory {
        fun build(): CourseInteractors {
            val cache = CourseCache.build()
            return CourseInteractors(
                GetMyCoursesUseCase(cache),
                GetPopularCoursesUseCase(cache)
            )
        }
    }
}