package com.clownteam.fuji.api.register

import com.google.gson.annotations.SerializedName

class RegisterRequest(
    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("repeat_password")
    val repeatPassword: String
)