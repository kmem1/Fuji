package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.token.TokenManager
import com.clownteam.course_datasource.network.CourseService
import com.clownteam.course_domain.CourseLesson
import com.clownteam.course_interactors.mappers.CourseLessonsResponseMapper

internal class GetCourseLessonsUseCase(
    private val service: CourseService,
    private val tokenManager: TokenManager
) : IGetCourseLessonsUseCase {

    override suspend fun invoke(param: GetCourseLessonsParams): GetCourseLessonsUseCaseResult {
        val token = tokenManager.getToken() ?: return GetCourseLessonsUseCaseResult.Unauthorized

        var result = service.getCourseLessons(token, param.courseId, param.moduleId)

        if (result.statusCode == 401) {
            val newTokenResponse = tokenManager.refreshToken()

            if (newTokenResponse.isNetworkError) {
                return GetCourseLessonsUseCaseResult.NetworkError
            }

            if (newTokenResponse.isSuccessCode && newTokenResponse.data != null) {
                newTokenResponse.data?.let {
                    result = service.getCourseLessons(token, param.courseId, param.moduleId)
                } ?: GetCourseLessonsUseCaseResult.Unauthorized
            } else {
                return GetCourseLessonsUseCaseResult.Unauthorized
            }
        }

        if (result.isNetworkError) return GetCourseLessonsUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.let {
                val mappedResult = it.map { model -> CourseLessonsResponseMapper.map(model) }
                GetCourseLessonsUseCaseResult.Success(mappedResult)
            } ?: GetCourseLessonsUseCaseResult.Failed
        } else {
            GetCourseLessonsUseCaseResult.Failed
        }
    }
}

data class GetCourseLessonsParams(
    val courseId: String,
    val moduleId: String
)

interface IGetCourseLessonsUseCase :
    IUseCase.InOut<GetCourseLessonsParams, GetCourseLessonsUseCaseResult>

sealed class GetCourseLessonsUseCaseResult {

    class Success(val data: List<CourseLesson>) : GetCourseLessonsUseCaseResult()

    object Failed : GetCourseLessonsUseCaseResult()

    object NetworkError : GetCourseLessonsUseCaseResult()

    object Unauthorized : GetCourseLessonsUseCaseResult()
}