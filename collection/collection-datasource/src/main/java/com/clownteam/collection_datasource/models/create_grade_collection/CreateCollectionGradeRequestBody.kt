package com.clownteam.collection_datasource.models.create_grade_collection

import com.google.gson.annotations.SerializedName

data class CreateCollectionGradeRequestBody(
    @SerializedName("grade")
    val grade: Int
)