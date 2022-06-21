package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.models.add_course_to_collection.AddCourseToCollectionBody
import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import com.clownteam.collection_datasource.models.get_collections.GetCollectionsResponse
import com.clownteam.collection_datasource.models.get_user_collections.GetUserCollectionsResponse
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

    override suspend fun getUserCollections(
        token: String,
        userPath: String
    ): NetworkResponse<GetUserCollectionsResponse> =
        baseRequest { api.getUserCollections("Bearer $token", userPath) }

    override suspend fun addCourseToCollection(
        token: String,
        courseId: String,
        collectionId: String
    ): NetworkResponse<Any> =
        baseRequest {
            api.addCourseToCollection(
                "Bearer $token",
                courseId,
                AddCourseToCollectionBody(collectionId)
            )
        }
}