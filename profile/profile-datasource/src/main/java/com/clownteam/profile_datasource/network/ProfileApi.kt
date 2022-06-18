package com.clownteam.profile_datasource.network

import com.clownteam.profile_datasource.network.models.ProfileResponse
import com.clownteam.profile_datasource.network.models.ProfilesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ProfileApi {

    @GET("api/profile")
    fun getProfile(@Header("Authorization") token: String): Call<ProfileResponse>
}