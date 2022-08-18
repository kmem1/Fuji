package com.clownteam.search_datasource

import com.clownteam.search_datasource.models.get_collections.GetCollectionsByQueryResponse
import com.clownteam.search_datasource.models.get_courses.GetCoursesByQueryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchApi {

    @GET("api/courses")
    fun searchCoursesByQuery(
        @Header("Authorization") token: String,
        @Query("search") query: String,
        @Query("page") page: Int
    ): Call<GetCoursesByQueryResponse>

    @GET("api/collections")
    fun searchCollectionsByQuery(
        @Header("Authorization") token: String,
        @Query("search") query: String,
        @Query("page") page: Int
    ): Call<GetCollectionsByQueryResponse>
}