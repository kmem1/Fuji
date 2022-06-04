package com.clownteam.course_interactors

import com.clownteam.course_datasource.cache.CourseCache
import com.clownteam.course_datasource.network.CourseApi
import com.clownteam.course_datasource.network.CourseService
import com.clownteam.course_datasource.network.CourseServiceImpl

class CourseInteractors private constructor(
    val getMyCourses: IGetMyCoursesUseCase,
    val getPopularCourses: IGetPopularCoursesUseCase,
    val getCourseById: IGetCourseByIdUseCase,
    val getCourseInfoById: IGetCourseInfoByIdUseCase
) {
    companion object Factory {
        fun build(api: CourseApi, baseUrl: String): CourseInteractors {
            val cache = CourseCache.build()
            val service = CourseServiceImpl(api)
            return CourseInteractors(
                GetMyCoursesUseCase(cache),
                GetPopularCoursesUseCase(cache, service, baseUrl),
                GetCourseByIdUseCase(cache),
                GetCourseInfoByIdUseCase(cache)
            )
        }
    }
}