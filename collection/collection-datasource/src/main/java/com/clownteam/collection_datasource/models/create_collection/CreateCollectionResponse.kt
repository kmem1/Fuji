package com.clownteam.collection_datasource.models.create_collection


import com.google.gson.annotations.SerializedName

data class CreateCollectionResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("title")
    val title: String?
)