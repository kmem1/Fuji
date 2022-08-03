package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.course_datasource.network.CourseService
import com.clownteam.course_datasource.network.models.get_course_steps.CourseStepsResponse
import com.clownteam.course_domain.CourseStep
import com.clownteam.course_interactors.mappers.CourseStepsResponseMapper

internal class GetCourseStepsUseCase(
    private val service: CourseService,
    private val tokenManager: TokenManager
) : IGetCourseStepsUseCase {

    override suspend fun invoke(param: GetCourseStepsParams): GetCourseStepsUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            getResponseFromServer(token, param)
        }

        if (result.isUnauthorized) return GetCourseStepsUseCaseResult.Unauthorized
        if (result.isNetworkError) return GetCourseStepsUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.let {
                val mappedResult = it.map { model -> CourseStepsResponseMapper.map(model) }
                GetCourseStepsUseCaseResult.Success(mappedResult)
            } ?: GetCourseStepsUseCaseResult.Failed
        } else {
            GetCourseStepsUseCaseResult.Failed
        }
    }

    private suspend fun getResponseFromServer(
        token: String,
        params: GetCourseStepsParams
    ): NetworkResponse<CourseStepsResponse> {
        return service.getCourseSteps(
            token,
            params.courseId,
            params.moduleId,
            params.lessonId,
            params.stepId
        )
    }
}

data class GetCourseStepsParams(
    val courseId: String,
    val moduleId: String,
    val lessonId: String,
    val stepId: String
)

interface IGetCourseStepsUseCase :
    IUseCase.InOut<GetCourseStepsParams, GetCourseStepsUseCaseResult>

sealed class GetCourseStepsUseCaseResult {

    class Success(val data: List<CourseStep>) : GetCourseStepsUseCaseResult()

    object Failed : GetCourseStepsUseCaseResult()

    object NetworkError : GetCourseStepsUseCaseResult()

    object Unauthorized : GetCourseStepsUseCaseResult()
}