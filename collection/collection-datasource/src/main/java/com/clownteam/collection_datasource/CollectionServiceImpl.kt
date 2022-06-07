package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.baseRequest

@Suppress("BlockingMethodInNonBlockingContext")
class CollectionServiceImpl(private val api: CollectionApi) : CollectionService {

    override suspend fun getCollections(token: String): NetworkResponse<GetCollectionResponse> =
        baseRequest { api.getCollections(token) }
}