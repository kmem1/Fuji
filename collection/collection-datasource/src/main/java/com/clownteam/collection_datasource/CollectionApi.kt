package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import com.clownteam.collection_datasource.models.get_collections.GetCollectionsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CollectionApi {

    @GET("api/collections/")
    fun getCollections(@Header("Authorization") token: String): Call<GetCollectionsResponse>

    @GET("api/collections/get/{path}")
    fun getCollection(
        @Header("Authorization") token: String,
        @Path("path") collectionPath: String
    ): Call<GetCollectionResponse>
}