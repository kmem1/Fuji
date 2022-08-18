package com.clownteam.search_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.search_datasource.SearchService
import com.clownteam.search_domain.SearchResultItem
import com.clownteam.search_interactors.mappers.CollectionSearchResultsMapper

internal class GetCollectionsByQueryUseCase(
    private val searchService: SearchService,
    val tokenManager: TokenManager,
    private val baseUrl: String
) : IGetCollectionsByQueryUseCase {

    override suspend fun invoke(param: GetCollectionsByQueryParams): GetCollectionsByQueryUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            searchService.getCollectionsByQuery(token, param.query, param.page)
        }

        if (result.isUnauthorized) GetCollectionsByQueryUseCaseResult.Unauthorized
        if (result.isNetworkError) GetCollectionsByQueryUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            if ((result.data?.pages ?: 0) < param.page) {
                return GetCollectionsByQueryUseCaseResult.Success(emptyList())
            }

            result.data?.results?.let {
                val mappedResult =
                    it.map { model -> CollectionSearchResultsMapper.map(model, baseUrl) }

                GetCollectionsByQueryUseCaseResult.Success(mappedResult)
            } ?: GetCollectionsByQueryUseCaseResult.Failed
        } else {
            GetCollectionsByQueryUseCaseResult.Failed
        }
    }
}

interface IGetCollectionsByQueryUseCase : IUseCase.InOut<GetCollectionsByQueryParams, GetCollectionsByQueryUseCaseResult>

data class GetCollectionsByQueryParams(
    val query: String,
    val page: Int
)

sealed class GetCollectionsByQueryUseCaseResult {
    class Success(val results: List<SearchResultItem.Collection>) :
        GetCollectionsByQueryUseCaseResult()

    object Failed : GetCollectionsByQueryUseCaseResult()

    object Unauthorized : GetCollectionsByQueryUseCaseResult()

    object NetworkError : GetCollectionsByQueryUseCaseResult()
}