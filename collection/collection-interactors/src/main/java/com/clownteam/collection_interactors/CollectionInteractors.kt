package com.clownteam.collection_interactors

import com.clownteam.collection_datasource.CollectionApi
import com.clownteam.collection_datasource.CollectionServiceImpl
import com.clownteam.core.network.token.TokenManager

class CollectionInteractors private constructor(
    val getMyCollections: IGetMyCollectionsUseCase
) {

    companion object Factory {
        fun build(
            api: CollectionApi,
            baseUrl: String,
            tokenManager: TokenManager
        ): CollectionInteractors {
            val service = CollectionServiceImpl(api)
            return CollectionInteractors(
                GetMyCollectionsUseCase(service, tokenManager, baseUrl)
            )
        }
    }
}