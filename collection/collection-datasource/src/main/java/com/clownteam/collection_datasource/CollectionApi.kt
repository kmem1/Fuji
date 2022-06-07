package com.clownteam.collection_datasource

import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CollectionApi {

    @GET("api/collections/")
    fun getCollections(@Header("Authorization") token: String): Call<GetCollectionResponse>
}