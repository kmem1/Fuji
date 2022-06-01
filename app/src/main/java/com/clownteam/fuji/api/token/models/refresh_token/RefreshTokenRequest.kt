package com.clownteam.fuji.api.token.models.refresh_token

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("refresh")
    val refresh: String?
)
