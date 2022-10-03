package com.clownteam.profile_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.profile_datasource.network.ProfileService
import com.clownteam.profile_domain.ProfileData
import com.clownteam.profile_interactors.mappers.ProfileDataMapper

internal class GetProfileUseCase(
    private val profileService: ProfileService,
    private val userDataManager: UserDataManager,
    private val tokenManager: TokenManager,
    private val baseUrl: String
) : IGetProfileUseCase {

    override suspend fun invoke(): GetProfileUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            profileService.getProfileData(token)
        }

        if (result.isUnauthorized) return GetProfileUseCaseResult.Unauthorized
        if (result.isNetworkError) return GetProfileUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.let {
                userDataManager.setUserPath(it.path ?: "")
                GetProfileUseCaseResult.Success(ProfileDataMapper.map(it, baseUrl))
            } ?: GetProfileUseCaseResult.Failed
        } else {
            GetProfileUseCaseResult.Failed
        }
    }
}

interface IGetProfileUseCase : IUseCase.Out<GetProfileUseCaseResult>

sealed class GetProfileUseCaseResult {

    class Success(val data: ProfileData) : GetProfileUseCaseResult()

    object Failed : GetProfileUseCaseResult()

    object Unauthorized : GetProfileUseCaseResult()

    object NetworkError : GetProfileUseCaseResult()
}