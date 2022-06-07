package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import com.clownteam.core.network.NetworkResponse

interface CollectionService {

    suspend fun getCollections(token: String): NetworkResponse<GetCollectionResponse>
}