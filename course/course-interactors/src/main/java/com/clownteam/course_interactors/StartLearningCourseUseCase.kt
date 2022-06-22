package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.token.TokenManager
import com.clownteam.course_datasource.network.CourseService

internal class StartLearningCourseUseCase(
    private val service: CourseService,
    private val tokenManager: TokenManager
) : IStartLearningCourseUseCase {

    override suspend fun invoke(param: String): StartLearningCourseUseCaseResult {
        val token = tokenManager.getToken() ?: return StartLearningCourseUseCaseResult.Unauthorized

        var result = service.startLearningCourse(token, courseId = param)

        if (result.statusCode == 401) {
            val newTokenResponse = tokenManager.refreshToken()

            if (newTokenResponse.isNetworkError) {
                return StartLearningCourseUseCaseResult.NetworkError
            }

            if (newTokenResponse.isSuccessCode && newTokenResponse.data != null) {
                newTokenResponse.data?.let {
                    result = service.startLearningCourse(token, courseId = param)
                } ?: StartLearningCourseUseCaseResult.Unauthorized
            } else {
                return StartLearningCourseUseCaseResult.Unauthorized
            }
        }

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