package com.clownteam.profile_datasource.network.models


import com.google.gson.annotations.SerializedName

data class ProfilesResponseItem(
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