package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.models.create_collection.CreateCollectionResponse
import com.clownteam.collection_datasource.models.create_grade_collection.CreateCollectionGradeRequestBody
import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import com.clownteam.collection_datasource.models.get_collections.GetCollectionsResponse
import com.clownteam.collection_datasource.models.get_user_collections.GetUserCollectionsResponse
import com.clownteam.collection_datasource.models.update_collection.UpdateCollectionRequestBody
import com.clownteam.collection_domain.CollectionSortOption
import com.clownteam.core.network.NetworkResponse

interface CollectionService {

    suspend fun getCollections(token: String): NetworkResponse<GetCollectionsResponse>

    suspend fun getCollection(
        token: String,
        collectionId: String
    ): NetworkResponse<GetCollectionResponse>

    suspend fun getUserCollections(
        token: String,
        userPath: String,
        search: String,
        page: Int,
        sortOption: CollectionSortOption
    ): NetworkResponse<GetUserCollectionsResponse>

    suspend fun addCourseToCollection(
        token: String,
        courseId: String,
        collectionId: String
    ): NetworkResponse<Any>

    suspend fun createCollection(token: String): NetworkResponse<CreateCollectionResponse>

    suspend fun updateCollection(
        token: String,
        collectionId: String,
        body: UpdateCollectionRequestBody
    ): NetworkResponse<Any>

    suspend fun createCollectionGrade(
        token: String,
        collectionId: String,
        body: CreateCollectionGradeRequestBody
    ): NetworkResponse<Any>
}