package com.clownteam.profile_datasource.network

import com.clownteam.profile_datasource.network.models.ProfilesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ProfileApi {

    @GET("api/profiles/")
    fun getProfiles(@Header("Authorization") token: String): Call<ProfilesResponse>
}