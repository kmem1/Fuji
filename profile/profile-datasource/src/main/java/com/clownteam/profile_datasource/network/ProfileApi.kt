package com.clownteam.profile_datasource.network

import com.clownteam.profile_datasource.network.models.get_collections.GetUserCollectionsResponse
import com.clownteam.profile_datasource.network.models.get_profile.ProfileResponse
import com.clownteam.profile_datasource.network.models.get_user_courses.GetUserCoursesResponse
import com.clownteam.profile_datasource.network.models.update_profile.UpdateProfileRequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProfileApi {

    @GET("api/profile")
    fun getProfile(@Header("Authorization") token: String): Call<ProfileResponse>

    @PUT("api/profiles/{user_path}/update/info/")
    fun updateProfileInfo(
        @Header("Authorization") token: String,
        @Body body: UpdateProfileRequestBody
    ): Call<Any>

    @GET("api/courses/all/{user_path}")
    fun getUserCourses(
        @Header("Authorization") token: String,
        @Path("user_path") userPath: String
    ): Call<GetUserCoursesResponse>

    @GET("api/collections/all/{user_path}")
    fun getUserCollections(
        @Header("Authorization") token: String,
        @Path("user_path") userPath: String
    ): Call<GetUserCollectionsResponse>
}