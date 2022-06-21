package com.clownteam.collection_datasource.models

import com.google.gson.annotations.SerializedName

data class CourseCollectionsAuthorResponseItem(
    @SerializedName("path")
    val path: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,
)

