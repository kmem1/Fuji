package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionService
import com.clownteam.collection_datasource.models.update_collection.UpdateCollectionRequestBody
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import java.io.File

internal class UpdateCollectionUseCase(
    private val service: CollectionService,
    private val tokenManager: TokenManager
) : IUpdateCollectionUseCase {

    override suspend fun invoke(param: UpdateCollectionUseCaseArgs): UpdateCollectionUseCaseResult {
        val body = UpdateCollectionRequestBody(
            title = param.newTitle,
            description = param.newDescription,
            image = param.newImage
        )

        val result = authorizationRequest(tokenManager) { token ->
            service.updateCollection(token, param.collectionId, body)
        }

        if (result.isUnauthorized) return UpdateCollectionUseCaseResult.Unauthorized
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
    val newTitle: String,
    val newDescription: String = "",
    val newImage: File? = null
)

interface IUpdateCollectionUseCase :
    IUseCase.InOut<UpdateCollectionUseCaseArgs, UpdateCollectionUseCaseResult>

sealed class UpdateCollectionUseCaseResult {

    object Success : UpdateCollectionUseCaseResult()

    object Failed : UpdateCollectionUseCaseResult()

    object NetworkError : UpdateCollectionUseCaseResult()

    object Unauthorized : UpdateCollectionUseCaseResult()
}