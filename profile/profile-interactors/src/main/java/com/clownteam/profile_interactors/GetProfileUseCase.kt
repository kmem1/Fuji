package com.clownteam.profile_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.token.TokenManager
import com.clownteam.profile_datasource.network.ProfileService
import com.clownteam.profile_domain.ProfileData
import com.clownteam.profile_interactors.mappers.ProfileDataMapper

internal class GetProfileUseCase(
    private val profileService: ProfileService,
    private val tokenManager: TokenManager
) : IGetProfileUseCase {

    override suspend fun invoke(): GetProfileUseCaseResult {
        val token = tokenManager.getToken() ?: return GetProfileUseCaseResult.Unauthorized

        var result = profileService.getProfileData(token)

        if (result.statusCode == 401) {
            val newTokenResponse = tokenManager.refreshToken()

            if (newTokenResponse.isNetworkError) {
                return GetProfileUseCaseResult.NetworkError
            }

            if (newTokenResponse.isSuccessCode && newTokenResponse.data != null) {
                newTokenResponse.data?.let {
                    result = profileService.getProfileData(it)
                } ?: GetProfileUseCaseResult.Unauthorized
            } else {
                return GetProfileUseCaseResult.Unauthorized
            }
        }

        if (result.isNetworkError) return GetProfileUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            val myProfile = result.data?.results?.first { it.isSubscribed == null }
                ?: return GetProfileUseCaseResult.Failed

            GetProfileUseCaseResult.Success(ProfileDataMapper.map(myProfile))
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