package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.mappers.CollectionSortOptionApiMapper
import com.clownteam.collection_datasource.models.add_course_to_collection.AddCourseToCollectionBody
import com.clownteam.collection_datasource.models.create_collection.CreateCollectionResponse
import com.clownteam.collection_datasource.models.create_grade_collection.CreateCollectionGradeRequestBody
import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import com.clownteam.collection_datasource.models.get_collections.GetCollectionsResponse
import com.clownteam.collection_datasource.models.get_user_collections.GetUserCollectionsResponse
import com.clownteam.collection_datasource.models.update_collection.UpdateCollectionRequestBody
import com.clownteam.collection_domain.CollectionSortOption
import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.baseRequest
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Suppress("BlockingMethodInNonBlockingContext")
class CollectionServiceImpl(private val api: CollectionApi) : CollectionService {

    override suspend fun getCollections(token: String): NetworkResponse<GetCollectionsResponse> =
        baseRequest { api.getCollections(token) }

    override suspend fun getCollection(
        token: String,
        collectionId: String
    ): NetworkResponse<GetCollectionResponse> =
        baseRequest { api.getCollection(token, collectionId) }

    override suspend fun getUserCollections(
        token: String,
        userPath: String,
        search: String,
        page: Int,
        sortOption: CollectionSortOption
    ): NetworkResponse<GetUserCollectionsResponse> =
        baseRequest {
            val ordering = CollectionSortOptionApiMapper.map(sortOption)
            api.getUserCollections(token, userPath, search, page, ordering)
        }

    override suspend fun addCourseToCollection(
        token: String,
        courseId: String,
        collectionId: String
    ): NetworkResponse<Any> =
        baseRequest {
            api.addCourseToCollection(
                token,
                courseId,
                AddCourseToCollectionBody(collectionId)
            )
        }

    override suspend fun createCollection(token: String): NetworkResponse<CreateCollectionResponse> =
        baseRequest { api.createCollection(token) }

    override suspend fun updateCollection(
        token: String,
        collectionId: String,
        body: UpdateCollectionRequestBody
    ): NetworkResponse<Any> =
        baseRequest {
            val multipartBody = body.image?.let { imageFile ->
                val imageRequestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)

                MultipartBody.Part.createFormData("image_url", body.image.name, imageRequestBody)
            }

            val titleBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), body.title)

            val descriptionBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), body.description)

            api.updateCollection(
                token, collectionId, titleBody, descriptionBody, multipartBody
            )
        }

    override suspend fun createCollectionGrade(
        token: String,
        collectionId: String,
        body: CreateCollectionGradeRequestBody
    ): NetworkResponse<Any> = baseRequest {
        api.createCollectionGrade(token, collectionId, body)
    }
}