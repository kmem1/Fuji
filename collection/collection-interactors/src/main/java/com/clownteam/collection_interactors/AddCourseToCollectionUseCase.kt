package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionService
import com.clownteam.collection_interactors.mappers.GetCollectionResponseItemMapper
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.token.TokenManager

internal class AddCourseToCollectionUseCase(
    private val service: CollectionService,
    private val tokenManager: TokenManager
) : IAddCourseToCollectionUseCase {

    override suspend fun invoke(param: AddCourseToCollectionParams): AddCourseToCollectionUseCaseResult {
        val token =
            tokenManager.getToken() ?: return AddCourseToCollectionUseCaseResult.Unauthorized

        var result = service.addCourseToCollection(token, param.courseId, param.collectionId)

        if (result.statusCode == 401) {
            val newTokenResponse = tokenManager.refreshToken()

            if (newTokenResponse.isNetworkError) {
                return AddCourseToCollectionUseCaseResult.NetworkError
            }

            if (newTokenResponse.isSuccessCode && newTokenResponse.data != null) {
                newTokenResponse.data?.let {
                    result = service.addCourseToCollection(token, param.courseId, param.collectionId)
                } ?: AddCourseToCollectionUseCaseResult.Unauthorized
            } else {
                return AddCourseToCollectionUseCaseResult.Unauthorized
            }
        }

        if (result.isNetworkError) return AddCourseToCollectionUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            return AddCourseToCollectionUseCaseResult.Success
        } else {
            AddCourseToCollectionUseCaseResult.Failed
        }
    }
}

data class AddCourseToCollectionParams(
    val courseId: String,
    val collectionId: String
)

interface IAddCourseToCollectionUseCase :
    IUseCase.InOut<AddCourseToCollectionParams, AddCourseToCollectionUseCaseResult>

sealed class AddCourseToCollectionUseCaseResult {

    object Success : AddCourseToCollectionUseCaseResult()

    object Failed : AddCourseToCollectionUseCaseResult()

    object NetworkError : AddCourseToCollectionUseCaseResult()

    object Unauthorized : AddCourseToCollectionUseCaseResult()
}