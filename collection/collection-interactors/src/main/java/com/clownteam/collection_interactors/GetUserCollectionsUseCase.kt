package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionService
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.collection_interactors.mappers.GetUserCollectionsResponseItemMapper
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager

internal class GetUserCollectionsUseCase(
    private val service: CollectionService,
    private val tokenManager: TokenManager,
    private val userDataManager: UserDataManager,
    private val baseUrl: String
) : IGetUserCollectionsUseCase {

    override suspend fun invoke(param: String): GetUserCollectionsUseCaseResult {
        val userPath =
            userDataManager.getUserPath() ?: return GetUserCollectionsUseCaseResult.Unauthorized

        val result = authorizationRequest(tokenManager) { token ->
            service.getUserCollections(token, userPath, param)
        }

        if (result.isUnauthorized) return GetUserCollectionsUseCaseResult.Unauthorized
        if (result.isNetworkError) return GetUserCollectionsUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.results?.let {
                val mappedResult =
                    it.map { model -> GetUserCollectionsResponseItemMapper.map(model, baseUrl) }

                val hasNextPage =
                    result.data?.next != null && result.data?.next?.isNotEmpty() == true

                GetUserCollectionsUseCaseResult.Success(mappedResult, hasNextPage)
            } ?: GetUserCollectionsUseCaseResult.Failed
        } else {
            GetUserCollectionsUseCaseResult.Failed
        }
    }
}

interface IGetUserCollectionsUseCase : IUseCase.InOut<String, GetUserCollectionsUseCaseResult>

sealed class GetUserCollectionsUseCaseResult {

    class Success(val data: List<CourseCollection>, val hasNextPage: Boolean) :
        GetUserCollectionsUseCaseResult()

    object Failed : GetUserCollectionsUseCaseResult()

    object NetworkError : GetUserCollectionsUseCaseResult()

    object Unauthorized : GetUserCollectionsUseCaseResult()
}
