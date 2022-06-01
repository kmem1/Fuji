package com.clownteam.authorization_datasource.network

import com.clownteam.authorization_datasource.network.login.LoginRequest
import com.clownteam.authorization_datasource.network.login.LoginResponse
import com.clownteam.authorization_datasource.network.register.RegisterRequest
import com.clownteam.authorization_datasource.network.register.RegisterResponse
import com.clownteam.authorization_domain.registration.RegistrationData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizationApi {

    @POST("api/register/")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("api/token/")
    fun token(@Body request: LoginRequest): Call<LoginResponse>
}