package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.models.add_course_to_collection.AddCourseToCollectionBody
import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import com.clownteam.collection_datasource.models.get_collections.GetCollectionsResponse
import com.clownteam.collection_datasource.models.get_user_collections.GetUserCollectionsResponse
import retrofit2.Call
import retrofit2.http.*

interface CollectionApi {

    @GET("api/collections/")
    fun getCollections(@Header("Authorization") token: String): Call<GetCollectionsResponse>

    @GET("api/collections/get/{path}")
    fun getCollection(
        @Header("Authorization") token: String,
        @Path("path") collectionPath: String
    ): Call<GetCollectionResponse>

    @GET("api/collections/created/{userPath}/")
    fun getUserCollections(
        @Header("Authorization") token: String,
        @Path("userPath") userPath: String
    ): Call<GetUserCollectionsResponse>

    @POST("api/courses/add/{courseId}/")
    fun addCourseToCollection(
        @Header("Authorization") token: String,
        @Path("courseId") courseId: String,
        @Body body: AddCourseToCollectionBody
    ): Call<Any>
}