package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionService
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.collection_interactors.mappers.GetCollectionResponseItemMapper
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager

internal class GetMyCollectionsUseCase(
    private val service: CollectionService,
    private val tokenManager: TokenManager,
    private val userDataManager: UserDataManager,
    private val baseUrl: String
) : IGetMyCollectionsUseCase {

    override suspend fun invoke(): GetMyCollectionsUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            service.getCollections(token)
        }

        if (result.isUnauthorized) return GetMyCollectionsUseCaseResult.Unauthorized
        if (result.isNetworkError) return GetMyCollectionsUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.results?.let {
                val userPath = userDataManager.getUserPath() ?: ""
                val mappedResult = it.map { model ->
                    GetCollectionResponseItemMapper.map(model, baseUrl, userPath)
                }
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

