package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.course_datasource.network.CourseService
import com.clownteam.course_datasource.network.models.get_course_step.CourseStepResponse
import com.clownteam.course_domain.CourseStep
import com.clownteam.course_interactors.mappers.CourseStepResponseMapper

internal class GetCourseStepUseCase(
    private val service: CourseService,
    private val tokenManager: TokenManager
) : IGetCourseStepUseCase {

    override suspend fun invoke(param: GetCourseStepParams): GetCourseStepUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            getResponseFromServer(token, param)
        }

        if (result.isUnauthorized) return GetCourseStepUseCaseResult.Unauthorized
        if (result.isNetworkError) return GetCourseStepUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.let {
                val mappedResult = CourseStepResponseMapper.map(it)
                GetCourseStepUseCaseResult.Success(mappedResult)
            } ?: GetCourseStepUseCaseResult.Failed
        } else {
            GetCourseStepUseCaseResult.Failed
        }
    }

    private suspend fun getResponseFromServer(
        token: String,
        params: GetCourseStepParams
    ): NetworkResponse<CourseStepResponse> {
        return service.getCourseStep(
            token,
            params.courseId,
            params.moduleId,
            params.lessonId,
            params.stepId
        )
    }
}

data class GetCourseStepParams(
    val courseId: String,
    val moduleId: String,
    val lessonId: String,
    val stepId: String
)

interface IGetCourseStepUseCase :
    IUseCase.InOut<GetCourseStepParams, GetCourseStepUseCaseResult>

sealed class GetCourseStepUseCaseResult {

    class Success(val data: CourseStep) : GetCourseStepUseCaseResult()

    object Failed : GetCourseStepUseCaseResult()

    object NetworkError : GetCourseStepUseCaseResult()

    object Unauthorized : GetCourseStepUseCaseResult()
}