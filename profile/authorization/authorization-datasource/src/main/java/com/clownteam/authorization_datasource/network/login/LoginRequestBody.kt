package com.clownteam.authorization_datasource.network.login

import com.google.gson.annotations.SerializedName

data class LoginRequestBody(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)