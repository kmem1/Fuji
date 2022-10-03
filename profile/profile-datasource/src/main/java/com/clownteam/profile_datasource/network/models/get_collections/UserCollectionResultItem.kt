package com.clownteam.profile_datasource.network.models.get_collections

import com.google.gson.annotations.SerializedName

open class UserCollectionResultItem {
    @SerializedName("author")
    val author: CollectionAuthorItem? = null

    @SerializedName("image_url")
    val imageUrl: String? = null

    @SerializedName("is_added")
    val isAdded: Boolean? = null

    @SerializedName("path")
    val path: String? = null

    @SerializedName("title")
    val title: String? = null
}

data class CollectionAuthorItem(
    @SerializedName("path")
    val path: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,
)
