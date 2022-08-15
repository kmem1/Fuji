package com.clownteam.search_datasource

import com.clownteam.search_datasource.models.GetCoursesByQueryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SearchApi {

    @GET("api/courses")
    fun searchCoursesByQuery(
        @Header("Authorization") token: String,
        @Path("search") query: String
    ): Call<GetCoursesByQueryResponse>
}