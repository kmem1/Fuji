package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import com.clownteam.collection_datasource.models.get_collections.GetCollectionsResponse
import com.clownteam.core.network.NetworkResponse

interface CollectionService {

    suspend fun getCollections(token: String): NetworkResponse<GetCollectionsResponse>

    suspend fun getCollection(token: String, collectionId: String): NetworkResponse<GetCollectionResponse>
}