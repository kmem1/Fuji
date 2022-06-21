package com.clownteam.collection_datasource.models.update_collection

import com.google.gson.annotations.SerializedName

data class UpdateCollectionResponseBody(
    @SerializedName("title")
    val title: String
)