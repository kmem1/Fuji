package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionService
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager

internal class CreateCollectionUseCase(
    private val service: CollectionService,
    private val tokenManager: TokenManager
) : ICreateCollectionUseCase {

    override suspend fun invoke(): CreateCollectionUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            service.createCollection(token)
        }

        if (result.isUnauthorized) return CreateCollectionUseCaseResult.Unauthorized
        if (result.isNetworkError) return CreateCollectionUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            return CreateCollectionUseCaseResult.Success(result.data?.path ?: "")
        } else {
            CreateCollectionUseCaseResult.Failed
        }
    }
}

interface ICreateCollectionUseCase : IUseCase.Out<CreateCollectionUseCaseResult>

sealed class CreateCollectionUseCaseResult {

    class Success(val collectionId: String) : CreateCollectionUseCaseResult()

    object Failed : CreateCollectionUseCaseResult()

    object NetworkError : CreateCollectionUseCaseResult()

    object Unauthorized : CreateCollectionUseCaseResult()
}