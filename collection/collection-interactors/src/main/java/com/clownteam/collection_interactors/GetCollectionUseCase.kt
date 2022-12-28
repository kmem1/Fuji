package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionService
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.collection_interactors.mappers.GetCollectionResponseItemMapper
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager

internal class GetCollectionUseCase(
    private val service: CollectionService,
    private val tokenManager: TokenManager,
    private val userDataManager: UserDataManager,
    private val baseUrl: String
) : IGetCollectionUseCase {

    override suspend fun invoke(param: String): GetCollectionUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            service.getCollection(token, param)
        }

        if (result.isUnauthorized) return GetCollectionUseCaseResult.Unauthorized
        if (result.isNetworkError) return GetCollectionUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            result.data?.let {
                val userPath = userDataManager.getUserPath() ?: ""
                val mappedResult = GetCollectionResponseItemMapper.map(it, baseUrl, userPath)
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