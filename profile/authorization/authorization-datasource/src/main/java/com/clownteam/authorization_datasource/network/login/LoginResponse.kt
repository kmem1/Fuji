package com.clownteam.authorization_datasource.network.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access")
    val access: String?,

    @SerializedName("refresh")
    val refresh: String?,
)