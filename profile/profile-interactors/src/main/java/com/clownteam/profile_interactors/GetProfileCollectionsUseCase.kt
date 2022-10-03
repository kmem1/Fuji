package com.clownteam.profile_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.profile_datasource.network.ProfileService
import com.clownteam.profile_domain.ProfileCollection
import com.clownteam.profile_interactors.mappers.UserCollectionResponseMapper

internal class GetProfileCollectionsUseCase(
    private val service: ProfileService,
    private val userDataManager: UserDataManager,
    private val tokenManager: TokenManager,
    private val baseUrl: String
) : IGetProfileCollectionsUseCase {

    override suspend fun invoke(): GetProfileCollectionsUseCaseResult {
        val username =
            userDataManager.getUserPath() ?: return GetProfileCollectionsUseCaseResult.Unauthorized

        val result = authorizationRequest(tokenManager) { token ->
            service.getUserCollections(token, username)
        }

        if (result.isUnauthorized) return GetProfileCollectionsUseCaseResult.Unauthorized
        if (result.isNetworkError) return GetProfileCollectionsUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.results?.let {
                val mappedResult =
                    it.map { model -> UserCollectionResponseMapper.map(model, baseUrl) }
                GetProfileCollectionsUseCaseResult.Success(mappedResult)
            } ?: GetProfileCollectionsUseCaseResult.Failed
        } else {
            GetProfileCollectionsUseCaseResult.Failed
        }
    }
}

interface IGetProfileCollectionsUseCase : IUseCase.Out<GetProfileCollectionsUseCaseResult>

sealed class GetProfileCollectionsUseCaseResult {

    class Success(val data: List<ProfileCollection>) : GetProfileCollectionsUseCaseResult()

    object Failed : GetProfileCollectionsUseCaseResult()

    object NetworkError : GetProfileCollectionsUseCaseResult()

    object Unauthorized : GetProfileCollectionsUseCaseResult()
}