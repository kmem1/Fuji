package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.course_datasource.network.CourseService
import com.clownteam.course_domain.Course
import com.clownteam.course_domain.CourseInfoUI
import com.clownteam.course_interactors.mappers.CourseInfoResponseMapper

internal class GetCourseInfoByIdUseCase(
    private val service: CourseService,
    private val tokenManager: TokenManager,
    private val baseUrl: String
) : IGetCourseInfoByIdUseCase {

    override suspend fun invoke(param: String): GetCourseInfoByIdUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            service.getCourseInfo(token, param)
        }

        if (result.isUnauthorized) return GetCourseInfoByIdUseCaseResult.Unauthorized
        if (result.isNetworkError) return GetCourseInfoByIdUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.let {
                val mappedResult = CourseInfoResponseMapper.map(it, baseUrl)
                GetCourseInfoByIdUseCaseResult.Success(
                    mappedResult.course,
                    mappedResult.courseInfoUI
                )
            } ?: GetCourseInfoByIdUseCaseResult.Failed
        } else {
            GetCourseInfoByIdUseCaseResult.Failed
        }
    }
}

interface IGetCourseInfoByIdUseCase :
    IUseCase.InOut<String, GetCourseInfoByIdUseCaseResult>

sealed class GetCourseInfoByIdUseCaseResult {

    class Success(val course: Course, val courseInfo: CourseInfoUI) :
        GetCourseInfoByIdUseCaseResult()

    object Failed : GetCourseInfoByIdUseCaseResult()

    object Unauthorized : GetCourseInfoByIdUseCaseResult()

    object NetworkError : GetCourseInfoByIdUseCaseResult()
}