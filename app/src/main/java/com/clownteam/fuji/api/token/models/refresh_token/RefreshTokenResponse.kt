package com.clownteam.fuji.api.token.models.refresh_token

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("access")
    val access: String?
)