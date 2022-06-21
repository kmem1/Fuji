package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.models.add_course_to_collection.AddCourseToCollectionBody
import com.clownteam.collection_datasource.models.create_collection.CreateCollectionResponse
import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import com.clownteam.collection_datasource.models.get_collections.GetCollectionsResponse
import com.clownteam.collection_datasource.models.get_user_collections.GetUserCollectionsResponse
import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.baseRequest

@Suppress("BlockingMethodInNonBlockingContext")
class CollectionServiceImpl(private val api: CollectionApi) : CollectionService {

    override suspend fun getCollections(token: String): NetworkResponse<GetCollectionsResponse> =
        baseRequest { api.getCollections(createBearerToken(token)) }

    override suspend fun getCollection(
        token: String,
        collectionId: String
    ): NetworkResponse<GetCollectionResponse> =
        baseRequest { api.getCollection(createBearerToken(token), collectionId) }

    override suspend fun getUserCollections(
        token: String,
        userPath: String
    ): NetworkResponse<GetUserCollectionsResponse> =
        baseRequest { api.getUserCollections(createBearerToken(token), userPath) }

    override suspend fun addCourseToCollection(
        token: String,
        courseId: String,
        collectionId: String
    ): NetworkResponse<Any> =
        baseRequest {
            api.addCourseToCollection(
                createBearerToken(token),
                courseId,
                AddCourseToCollectionBody(collectionId)
            )
        }

    override suspend fun createCollection(token: String): NetworkResponse<CreateCollectionResponse> =
        baseRequest { api.createCollection(createBearerToken(token)) }

    private fun createBearerToken(token: String): String {
        return "Bearer $token"
    }
}