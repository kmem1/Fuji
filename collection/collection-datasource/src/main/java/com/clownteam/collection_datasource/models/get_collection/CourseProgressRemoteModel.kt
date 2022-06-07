package com.clownteam.collection_datasource.models.get_collection

import com.google.gson.annotations.SerializedName

data class CourseProgressRemoteModel(
    @SerializedName("max_progress")
    val maxProgress: Int?,
    @SerializedName("progress")
    val progress: Int?
)