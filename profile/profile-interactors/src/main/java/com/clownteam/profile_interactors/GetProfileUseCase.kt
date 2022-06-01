package com.clownteam.profile_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.profile_datasource.network.ProfileService
import com.clownteam.profile_domain.ProfileData
import com.clownteam.profile_interactors.mappers.ProfileDataMapper

internal class GetProfileUseCase(private val profileService: ProfileService) : IGetProfileUseCase {

    override suspend fun invoke(param: String): GetProfileUseCaseResult {
        val result = profileService.getProfileData(param)

        if (result.statusCode == 401) return GetProfileUseCaseResult.TokenExpired
        if (result.isNetworkError) return GetProfileUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            val myProfile = result.data?.first { it.isSubscribed == null }
                ?: return GetProfileUseCaseResult.Failed

            GetProfileUseCaseResult.Success(ProfileDataMapper.map(myProfile))
        } else {
            GetProfileUseCaseResult.Failed
        }
    }
}

interface IGetProfileUseCase : IUseCase.InOut<String, GetProfileUseCaseResult>

sealed class GetProfileUseCaseResult {

    class Success(val data: ProfileData) : GetProfileUseCaseResult()

    object Failed : GetProfileUseCaseResult()

    object TokenExpired : GetProfileUseCaseResult()

    object NetworkError : GetProfileUseCaseResult()
}