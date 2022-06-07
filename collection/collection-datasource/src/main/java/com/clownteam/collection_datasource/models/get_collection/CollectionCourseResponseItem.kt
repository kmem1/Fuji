package com.clownteam.collection_datasource.models.get_collection


import com.google.gson.annotations.SerializedName

data class CollectionCourseResponseItem(
    @SerializedName("author")
    val author: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("duration_in_minutes")
    val durationInMinutes: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("members_amount")
    val membersAmount: Int?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("progress")
    val progress: CourseProgressRemoteModel?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("status_progress")
    val statusProgress: Any?,
    @SerializedName("title")
    val title: String?
)