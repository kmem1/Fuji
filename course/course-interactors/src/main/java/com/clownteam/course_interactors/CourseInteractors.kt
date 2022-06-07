package com.clownteam.course_interactors

import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.course_datasource.cache.CourseCache
import com.clownteam.course_datasource.network.CourseApi
import com.clownteam.course_datasource.network.CourseServiceImpl

class CourseInteractors private constructor(
    val getMyCourses: IGetMyCoursesUseCase,
    val getPopularCourses: IGetPopularCoursesUseCase,
    val getCourseById: IGetCourseByIdUseCase,
    val getCourseInfoById: IGetCourseInfoByIdUseCase
) {
    companion object Factory {
        fun build(
            api: CourseApi,
            baseUrl: String,
            tokenManager: TokenManager,
            userDataManager: UserDataManager
        ): CourseInteractors {
            val cache = CourseCache.build()
            val service = CourseServiceImpl(api)
            return CourseInteractors(
                GetMyCoursesUseCase(service, userDataManager, tokenManager, baseUrl),
                GetPopularCoursesUseCase(service, tokenManager, baseUrl),
                GetCourseByIdUseCase(cache),
                GetCourseInfoByIdUseCase(service, tokenManager, baseUrl)
            )
        }
    }
}