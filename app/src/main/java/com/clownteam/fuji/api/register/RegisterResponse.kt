package com.clownteam.fuji.api.register


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("username")
    val username: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("user")
    val user: User?
)

data class User(
    @SerializedName("email")
    val email: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("username")
    val username: String?
)