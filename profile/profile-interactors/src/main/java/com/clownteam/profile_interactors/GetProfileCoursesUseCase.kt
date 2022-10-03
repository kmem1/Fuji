package com.clownteam.profile_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.profile_datasource.network.ProfileService
import com.clownteam.profile_domain.ProfileCourse
import com.clownteam.profile_interactors.mappers.UserCourseResponseMapper

internal class GetProfileCoursesUseCase(
    private val service: ProfileService,
    private val userDataManager: UserDataManager,
    private val tokenManager: TokenManager,
    private val baseUrl: String
) : IGetProfileCoursesUseCase {

    override suspend fun invoke(): GetProfileCoursesUseCaseResult {
        val username =
            userDataManager.getUserPath() ?: return GetProfileCoursesUseCaseResult.Unauthorized

        val result = authorizationRequest(tokenManager) { token ->
            service.getUserCourses(token, username)
        }

        if (result.isUnauthorized) return GetProfileCoursesUseCaseResult.Unauthorized
        if (result.isNetworkError) return GetProfileCoursesUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.results?.let {
                val mappedResult = it.map { model -> UserCourseResponseMapper.map(model, baseUrl) }
                GetProfileCoursesUseCaseResult.Success(mappedResult)
            } ?: GetProfileCoursesUseCaseResult.Failed
        } else {
            GetProfileCoursesUseCaseResult.Failed
        }
    }
}

interface IGetProfileCoursesUseCase : IUseCase.Out<GetProfileCoursesUseCaseResult>

sealed class GetProfileCoursesUseCaseResult {

    class Success(val data: List<ProfileCourse>) : GetProfileCoursesUseCaseResult()

    object Failed : GetProfileCoursesUseCaseResult()

    object NetworkError : GetProfileCoursesUseCaseResult()

    object Unauthorized : GetProfileCoursesUseCaseResult()
}
