package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionService
import com.clownteam.collection_datasource.models.update_collection.UpdateCollectionResponseBody
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.token.TokenManager

internal class UpdateCollectionUseCase(
    private val service: CollectionService,
    private val tokenManager: TokenManager
) : IUpdateCollectionUseCase {

    override suspend fun invoke(param: UpdateCollectionUseCaseArgs): UpdateCollectionUseCaseResult {
        val token =
            tokenManager.getToken() ?: return UpdateCollectionUseCaseResult.Unauthorized

        val body = UpdateCollectionResponseBody(param.newTitle)
        var result = service.updateCollection(token, param.collectionId, body)

        if (result.statusCode == 401) {
            val newTokenResponse = tokenManager.refreshToken()

            if (newTokenResponse.isNetworkError) {
                return UpdateCollectionUseCaseResult.NetworkError
            }

            if (newTokenResponse.isSuccessCode && newTokenResponse.data != null) {
                newTokenResponse.data?.let {
                    result = service.updateCollection(token, param.collectionId, body)
                } ?: UpdateCollectionUseCaseResult.Unauthorized
            } else {
                return UpdateCollectionUseCaseResult.Unauthorized
            }
        }

        if (result.isNetworkError) return UpdateCollectionUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            return UpdateCollectionUseCaseResult.Success
        } else {
            UpdateCollectionUseCaseResult.Failed
        }
    }
}

data class UpdateCollectionUseCaseArgs(
    val collectionId: String,
    val newTitle: String
)

interface IUpdateCollectionUseCase :
    IUseCase.InOut<UpdateCollectionUseCaseArgs, UpdateCollectionUseCaseResult>

sealed class UpdateCollectionUseCaseResult {

    object Success : UpdateCollectionUseCaseResult()

    object Failed : UpdateCollectionUseCaseResult()

    object NetworkError : UpdateCollectionUseCaseResult()

    object Unauthorized : UpdateCollectionUseCaseResult()
}