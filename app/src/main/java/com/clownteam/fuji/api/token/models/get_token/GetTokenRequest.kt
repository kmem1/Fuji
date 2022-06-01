package com.clownteam.fuji.api.token.models.get_token


import com.google.gson.annotations.SerializedName

data class GetTokenRequest(
    @SerializedName("password")
    val password: String?,
    @SerializedName("username")
    val username: String?
)