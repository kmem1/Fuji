package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.course_datasource.network.CourseService

internal class StartLearningCourseUseCase(
    private val service: CourseService,
    private val tokenManager: TokenManager
) : IStartLearningCourseUseCase {

    override suspend fun invoke(param: String): StartLearningCourseUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            service.startLearningCourse(token, courseId = param)
        }

        if (result.isUnauthorized) return StartLearningCourseUseCaseResult.Unauthorized
        if (result.isNetworkError) return StartLearningCourseUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            StartLearningCourseUseCaseResult.Success
        } else {
            StartLearningCourseUseCaseResult.Failed
        }
    }
}

interface IStartLearningCourseUseCase : IUseCase.InOut<String, StartLearningCourseUseCaseResult>

sealed class StartLearningCourseUseCaseResult {

    object Success : StartLearningCourseUseCaseResult()

    object Failed : StartLearningCourseUseCaseResult()

    object Unauthorized : StartLearningCourseUseCaseResult()

    object NetworkError : StartLearningCourseUseCaseResult()
}