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
    val getCourseInfoById: IGetCourseInfoByIdUseCase,
    val getCourseModulesUseCase: IGetCourseModulesUseCase,
    val getCourseLessonsUseCase: IGetCourseLessonsUseCase,
    val getCourseStepsUseCase: IGetCourseStepsUseCase,
    val getCourseStepUseCase: IGetCourseStepUseCase,
    val startLearningCourseUseCase: IStartLearningCourseUseCase
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
                GetCourseInfoByIdUseCase(service, tokenManager, baseUrl),
                GetCourseModulesUseCase(service, tokenManager),
                GetCourseLessonsUseCase(service, tokenManager),
                GetCourseStepsUseCase(service, tokenManager),
                GetCourseStepUseCase(service, tokenManager),
                StartLearningCourseUseCase(service, tokenManager)
            )
        }
    }
}