package com.clownteam.authorization_datasource.network.user_path

import com.google.gson.annotations.SerializedName

data class UserPathResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("is_subscribed")
    val isSubscribed: Boolean?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("username")
    val username: String?
)
