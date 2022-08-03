package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
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
        val username =
            userDataManager.getUserPath() ?: return GetMyCoursesUseCaseResult.Unauthorized

        val result = authorizationRequest(tokenManager) { token ->
            service.getUserCourses(token, username)
        }

        if (result.isUnauthorized) return GetMyCoursesUseCaseResult.Unauthorized
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
