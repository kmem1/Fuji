package com.clownteam.user_archive_datasource

import com.clownteam.user_archive_datasource.models.get_collections.GetArchiveCollectionsResponse
import com.clownteam.user_archive_datasource.models.get_courses.GetArchiveCoursesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UserArchiveApi {

    @GET("api/courses/all/{userPath}")
    fun getArchiveCourses(
        @Header("Authorization") token: String,
        @Path("userPath") userPath: String,
        @Query("search") query: String,
        @Query("page") page: Int
    ): Call<GetArchiveCoursesResponse>

    @GET("api/collections/all/{userPath}")
    fun getArchiveCollections(
        @Header("Authorization") token: String,
        @Path("userPath") userPath: String,
        @Query("search") query: String,
        @Query("page") page: Int
    ): Call<GetArchiveCollectionsResponse>
}