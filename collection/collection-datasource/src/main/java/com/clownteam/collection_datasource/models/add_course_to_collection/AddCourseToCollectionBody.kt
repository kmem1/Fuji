package com.clownteam.collection_datasource.models.add_course_to_collection

import com.google.gson.annotations.SerializedName

data class AddCourseToCollectionBody(
    @SerializedName("collection_path")
    val collectionPath: String
)