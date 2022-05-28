package com.clownteam.fuji.api

import com.clownteam.fuji.api.register.RegisterRequest
import com.clownteam.fuji.api.register.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FujiService {

    @POST("api/register/")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}