package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.course_datasource.network.CourseService
import com.clownteam.course_domain.CourseModule
import com.clownteam.course_interactors.mappers.CourseModulesResponseMapper

internal class GetCourseModulesUseCase(
    private val service: CourseService,
    private val tokenManager: TokenManager
) : IGetCourseModulesUseCase {

    override suspend fun invoke(param: String): GetCourseModulesUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            service.getCourseModules(token, param)
        }

        if (result.isUnauthorized) return GetCourseModulesUseCaseResult.Unauthorized
        if (result.isNetworkError) return GetCourseModulesUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.let {
                val mappedResult = it.map { model -> CourseModulesResponseMapper.map(model) }
                GetCourseModulesUseCaseResult.Success(mappedResult)
            } ?: GetCourseModulesUseCaseResult.Failed
        } else {
            GetCourseModulesUseCaseResult.Failed
        }
    }
}

interface IGetCourseModulesUseCase : IUseCase.InOut<String, GetCourseModulesUseCaseResult>

sealed class GetCourseModulesUseCaseResult {

    class Success(val data: List<CourseModule>) : GetCourseModulesUseCaseResult()

    object Failed : GetCourseModulesUseCaseResult()

    object NetworkError : GetCourseModulesUseCaseResult()

    object Unauthorized : GetCourseModulesUseCaseResult()
}
