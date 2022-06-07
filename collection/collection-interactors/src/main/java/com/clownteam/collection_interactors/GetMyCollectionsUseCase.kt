package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionService
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.collection_interactors.mappers.GetCollectionResponseItemMapper
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager

internal class GetMyCollectionsUseCase(
    private val service: CollectionService,
    private val tokenManager: TokenManager,
    private val baseUrl: String
) : IGetMyCollectionsUseCase {

    override suspend fun invoke(): GetMyCollectionsUseCaseResult {
        val token = tokenManager.getToken() ?: return GetMyCollectionsUseCaseResult.Unauthorized

        var result = service.getCollections(token)

        if (result.statusCode == 401) {
            val newTokenResponse = tokenManager.refreshToken()

            if (newTokenResponse.isNetworkError) {
                return GetMyCollectionsUseCaseResult.NetworkError
            }

            if (newTokenResponse.isSuccessCode && newTokenResponse.data != null) {
                newTokenResponse.data?.let {
                    result = service.getCollections(token)
                } ?: GetMyCollectionsUseCaseResult.Unauthorized
            } else {
                return GetMyCollectionsUseCaseResult.Unauthorized
            }
        }

        if (result.isNetworkError) return GetMyCollectionsUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.results?.let {
                val mappedResult =
                    it.map { model -> GetCollectionResponseItemMapper.map(model, baseUrl) }
                GetMyCollectionsUseCaseResult.Success(mappedResult)
            } ?: GetMyCollectionsUseCaseResult.Failed
        } else {
            GetMyCollectionsUseCaseResult.Failed
        }
    }
}

interface IGetMyCollectionsUseCase : IUseCase.Out<GetMyCollectionsUseCaseResult>

sealed class GetMyCollectionsUseCaseResult {

    class Success(val data: List<CourseCollection>) : GetMyCollectionsUseCaseResult()

    object Failed : GetMyCollectionsUseCaseResult()

    object NetworkError : GetMyCollectionsUseCaseResult()

    object Unauthorized : GetMyCollectionsUseCaseResult()
}

