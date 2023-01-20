package com.clownteam.collection_datasource.models.update_collection

import com.google.gson.annotations.SerializedName
import java.io.File

data class UpdateCollectionRequestBody(
    val title: String,
    val description: String,
    val image: File? = null
)
