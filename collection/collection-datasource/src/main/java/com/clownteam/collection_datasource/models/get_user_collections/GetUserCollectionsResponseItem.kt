package com.clownteam.collection_datasource.models.get_user_collections


import com.google.gson.annotations.SerializedName

data class GetUserCollectionsResponseItem(
    @SerializedName("author")
    val author: AuthorModel?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("is_added")
    val isAdded: Boolean?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("title")
    val title: String?
)

data class AuthorModel(
    @SerializedName("path")
    val id: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?
)