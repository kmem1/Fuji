package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import com.clownteam.collection_datasource.models.get_collections.GetCollectionsResponse
import com.clownteam.collection_datasource.models.get_user_collections.GetUserCollectionsResponse
import com.clownteam.core.network.NetworkResponse

interface CollectionService {

    suspend fun getCollections(token: String): NetworkResponse<GetCollectionsResponse>

    suspend fun getCollection(token: String, collectionId: String): NetworkResponse<GetCollectionResponse>

    suspend fun getUserCollections(token: String, userPath: String): NetworkResponse<GetUserCollectionsResponse>

    suspend fun addCourseToCollection(token: String, courseId: String, collectionId: String): NetworkResponse<Any>

}