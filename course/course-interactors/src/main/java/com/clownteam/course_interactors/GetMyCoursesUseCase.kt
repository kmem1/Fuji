package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.course_datasource.network.CourseService
import com.clownteam.course_domain.Course
import com.clownteam.course_interactors.mappers.UserCourseResponseMapper

internal class GetMyCoursesUseCase(
    private val service: CourseService,
    private val userDataManager: UserDataManager,
    private val tokenManager: TokenManager,
    private val baseUrl: String
) : IGetMyCoursesUseCase {

    override suspend fun invoke(): GetMyCoursesUseCaseResult {
        val token = tokenManager.getToken() ?: return GetMyCoursesUseCaseResult.Unauthorized
        val username =
            userDataManager.getUserPath() ?: return GetMyCoursesUseCaseResult.Unauthorized

        println("username: $username")

        var result = service.getUserCourses(token, username)

        if (result.statusCode == 401) {
            val newTokenResponse = tokenManager.refreshToken()

            if (newTokenResponse.isNetworkError) {
                return GetMyCoursesUseCaseResult.NetworkError
            }

            if (newTokenResponse.isSuccessCode && newTokenResponse.data != null) {
                newTokenResponse.data?.let {
                    result = service.getUserCourses(token, username)
                } ?: GetMyCoursesUseCaseResult.Unauthorized
            } else {
                return GetMyCoursesUseCaseResult.Unauthorized
            }
        }

        if (result.isNetworkError) return GetMyCoursesUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.results?.let {
                val mappedResult = it.map { model -> UserCourseResponseMapper.map(model, baseUrl) }
                GetMyCoursesUseCaseResult.Success(mappedResult)
            } ?: GetMyCoursesUseCaseResult.Failed
        } else {
            GetMyCoursesUseCaseResult.Failed
        }
    }
}

interface IGetMyCoursesUseCase : IUseCase.Out<GetMyCoursesUseCaseResult>

sealed class GetMyCoursesUseCaseResult {

    class Success(val data: List<Course>) : GetMyCoursesUseCaseResult()

    object Failed : GetMyCoursesUseCaseResult()

    object NetworkError : GetMyCoursesUseCaseResult()

    object Unauthorized : GetMyCoursesUseCaseResult()
}
