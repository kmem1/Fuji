package com.clownteam.course_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.course_datasource.network.CourseService
import com.clownteam.course_datasource.network.models.get_courses.CourseInfoRemoteModel
import com.clownteam.course_domain.Course
import com.clownteam.course_interactors.mappers.CourseInfoRemoteModelMapper

internal class GetPopularCoursesUseCase(
    private val service: CourseService,
    private val tokenManager: TokenManager,
    private val baseUrl: String
) : IGetPopularCoursesUseCase {

    override suspend fun invoke(): GetPopularCoursesUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            service.getCourses(token)
        }

        if (result.isUnauthorized) return GetPopularCoursesUseCaseResult.Unauthorized
        if (result.isNetworkError) return GetPopularCoursesUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data?.results != null) {
            result.data?.results?.let { models ->
                val courses = mapModelsToCourses(models)
                GetPopularCoursesUseCaseResult.Success(courses)
            } ?: GetPopularCoursesUseCaseResult.Failed
        } else {
            GetPopularCoursesUseCaseResult.Failed
        }
    }

    private fun mapModelsToCourses(models: List<CourseInfoRemoteModel>): List<Course> {
        return models.map { CourseInfoRemoteModelMapper.map(it, baseUrl) }
    }
}

interface IGetPopularCoursesUseCase : IUseCase.Out<GetPopularCoursesUseCaseResult>

sealed class GetPopularCoursesUseCaseResult {

    class Success(val data: List<Course>) : GetPopularCoursesUseCaseResult()

    object Failed : GetPopularCoursesUseCaseResult()

    object Unauthorized : GetPopularCoursesUseCaseResult()

    object NetworkError : GetPopularCoursesUseCaseResult()
}