package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionService
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.collection_interactors.mappers.GetUserCollectionsResponseItemMapper
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager

internal class GetUserCollectionsUseCase(
    private val service: CollectionService,
    private val tokenManager: TokenManager,
    private val userDataManager: UserDataManager,
    private val baseUrl: String
) : IGetUserCollectionsUseCase {

    override suspend fun invoke(): GetUserCollectionsUseCaseResult {
        val token = tokenManager.getToken() ?: return GetUserCollectionsUseCaseResult.Unauthorized
        val userPath =
            userDataManager.getUserPath() ?: return GetUserCollectionsUseCaseResult.Unauthorized

        var result = service.getUserCollections(token, userPath)

        if (result.statusCode == 401) {
            val newTokenResponse = tokenManager.refreshToken()

            if (newTokenResponse.isNetworkError) {
                return GetUserCollectionsUseCaseResult.NetworkError
            }

            if (newTokenResponse.isSuccessCode && newTokenResponse.data != null) {
                newTokenResponse.data?.let {
                    result = service.getUserCollections(token, userPath)
                } ?: GetUserCollectionsUseCaseResult.Unauthorized
            } else {
                return GetUserCollectionsUseCaseResult.Unauthorized
            }
        }

        if (result.isNetworkError) return GetUserCollectionsUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.results?.let {
                val mappedResult =
                    it.map { model -> GetUserCollectionsResponseItemMapper.map(model, baseUrl) }
                GetUserCollectionsUseCaseResult.Success(mappedResult)
            } ?: GetUserCollectionsUseCaseResult.Failed
        } else {
            GetUserCollectionsUseCaseResult.Failed
        }
    }
}

interface IGetUserCollectionsUseCase : IUseCase.Out<GetUserCollectionsUseCaseResult>

sealed class GetUserCollectionsUseCaseResult {

    class Success(val data: List<CourseCollection>) : GetUserCollectionsUseCaseResult()

    object Failed : GetUserCollectionsUseCaseResult()

    object NetworkError : GetUserCollectionsUseCaseResult()

    object Unauthorized : GetUserCollectionsUseCaseResult()
}
