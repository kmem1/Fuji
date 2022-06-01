package com.clownteam.fuji.api.token.models.get_token

import com.google.gson.annotations.SerializedName

data class GetTokenResponse(
    @SerializedName("access")
    val access: String?,
    @SerializedName("refresh")
    val refresh: String?
)