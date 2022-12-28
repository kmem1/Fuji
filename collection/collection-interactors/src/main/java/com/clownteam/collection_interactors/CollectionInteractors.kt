package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionApi
import com.clownteam.collection_datasource.CollectionServiceImpl
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager

class CollectionInteractors private constructor(
    val getMyCollections: IGetMyCollectionsUseCase,
    val getCollection: IGetCollectionUseCase,
    val getUserCollections: IGetUserCollectionsUseCase,
    val addCourseToCollection: IAddCourseToCollectionUseCase,
    val createCollection: ICreateCollectionUseCase,
    val updateCollection: IUpdateCollectionUseCase
) {
    companion object Factory {
        fun build(
            api: CollectionApi,
            baseUrl: String,
            tokenManager: TokenManager,
            userManager: UserDataManager
        ): CollectionInteractors {
            val service = CollectionServiceImpl(api)
            return CollectionInteractors(
                GetMyCollectionsUseCase(service, tokenManager, userManager, baseUrl),
                GetCollectionUseCase(service, tokenManager, userManager, baseUrl),
                GetUserCollectionsUseCase(service, tokenManager, userManager, baseUrl),
                AddCourseToCollectionUseCase(service, tokenManager),
                CreateCollectionUseCase(service, tokenManager),
                UpdateCollectionUseCase(service, tokenManager)
            )
        }
    }
}