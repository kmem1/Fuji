package com.clownteam.collection_datasource.models


import com.google.gson.annotations.SerializedName

open class CollectionCourseResponseItem {
    @SerializedName("author")
    val author: String? = null

    @SerializedName("description")
    val description: String? = null

    @SerializedName("duration_in_minutes")
    val durationInMinutes: Int? = null

    @SerializedName("image_url")
    val imageUrl: String? = null

    @SerializedName("members_amount")
    val membersAmount: Int? = null

    @SerializedName("path")
    val path: String? = null

    @SerializedName("progress")
    val progress: CourseProgressRemoteModel? = null

    @SerializedName("rating")
    val rating: Double? = null

    @SerializedName("status_progress")
    val statusProgress: String? = null

    @SerializedName("title")
    val title: String? = null
}