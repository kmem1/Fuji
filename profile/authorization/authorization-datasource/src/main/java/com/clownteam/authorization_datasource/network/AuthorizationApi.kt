package com.clownteam.authorization_datasource.network

import com.clownteam.authorization_datasource.network.login.LoginRequestBody
import com.clownteam.authorization_datasource.network.login.LoginResponse
import com.clownteam.authorization_datasource.network.register.RegisterRequestBody
import com.clownteam.authorization_datasource.network.register.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizationApi {

    @POST("api/register/")
    fun register(@Body request: RegisterRequestBody): Call<RegisterResponse>

    @POST("api/token/")
    fun token(@Body request: LoginRequestBody): Call<LoginResponse>

    @POST("api/request-reset-email/")
    fun restorePassword(@Body email: String): Call<Any>
}