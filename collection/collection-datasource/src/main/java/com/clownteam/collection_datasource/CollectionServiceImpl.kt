package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import com.clownteam.collection_datasource.models.get_collections.GetCollectionsResponse
import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.baseRequest

@Suppress("BlockingMethodInNonBlockingContext")
class CollectionServiceImpl(private val api: CollectionApi) : CollectionService {

    override suspend fun getCollections(token: String): NetworkResponse<GetCollectionsResponse> =
        baseRequest { api.getCollections("Bearer $token") }

    override suspend fun getCollection(
        token: String,
        collectionId: String
    ): NetworkResponse<GetCollectionResponse> =
        baseRequest { api.getCollection("Bearer $token", collectionId) }
}