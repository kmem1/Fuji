package com.clownteam.course_datasource.network.models.get_courses


import com.google.gson.annotations.SerializedName

data class CourseInfoRemoteModel(
    @SerializedName("author")
    val author: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("duration_in_minutes")
    val durationInMinutes: Int?,
    @SerializedName("members_amount")
    val membersAmount: Int?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("progress")
    val progress: CourseProgressRemoteModel?,
    @SerializedName("quantity_in_collection")
    val quantityInCollection: Int?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("status_progress")
    val statusProgress: String?,
    @SerializedName("title")
    val title: String?
)