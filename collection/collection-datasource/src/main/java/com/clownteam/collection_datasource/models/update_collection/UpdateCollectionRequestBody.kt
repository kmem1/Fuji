package com.clownteam.collection_datasource.models.update_collection

import com.google.gson.annotations.SerializedName

data class UpdateCollectionRequestBody(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String
)
