package com.clownteam.fuji.api.token

import com.clownteam.fuji.api.token.models.get_token.GetTokenRequest
import com.clownteam.fuji.api.token.models.get_token.GetTokenResponse
import com.clownteam.fuji.api.token.models.refresh_token.RefreshTokenRequest
import com.clownteam.fuji.api.token.models.refresh_token.RefreshTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {

    @POST("api/token/")
    fun getToken(@Body getTokenRequest: GetTokenRequest): Call<GetTokenResponse>

    @POST("api/refresh_token/")
    fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Call<RefreshTokenResponse>
}