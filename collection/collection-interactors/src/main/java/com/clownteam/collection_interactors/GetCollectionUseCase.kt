package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionService
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.collection_interactors.mappers.GetCollectionResponseItemMapper
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.token.TokenManager

internal class GetCollectionUseCase(
    private val service: CollectionService,
    private val tokenManager: TokenManager,
    private val baseUrl: String
) : IGetCollectionUseCase {

    override suspend fun invoke(param: String): GetCollectionUseCaseResult {
        val token = tokenManager.getToken() ?: return GetCollectionUseCaseResult.Unauthorized

        var result = service.getCollection(token, param)

        if (result.statusCode == 401) {
            val newTokenResponse = tokenManager.refreshToken()

            if (newTokenResponse.isNetworkError) {
                return GetCollectionUseCaseResult.NetworkError
            }

            if (newTokenResponse.isSuccessCode && newTokenResponse.data != null) {
                newTokenResponse.data?.let {
                    result = service.getCollection(token, param)
                } ?: GetCollectionUseCaseResult.Unauthorized
            } else {
                return GetCollectionUseCaseResult.Unauthorized
            }
        }

        if (result.isNetworkError) return GetCollectionUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.let {
                val mappedResult = GetCollectionResponseItemMapper.map(it, baseUrl)
                GetCollectionUseCaseResult.Success(mappedResult)
            } ?: GetCollectionUseCaseResult.Failed
        } else {
            GetCollectionUseCaseResult.Failed
        }
    }
}

interface IGetCollectionUseCase : IUseCase.InOut<String, GetCollectionUseCaseResult>

sealed class GetCollectionUseCaseResult {

    class Success(val data: CourseCollection) : GetCollectionUseCaseResult()

    object Failed : GetCollectionUseCaseResult()

    object NetworkError : GetCollectionUseCaseResult()

    object Unauthorized : GetCollectionUseCaseResult()
}