package com.clownteam.course_datasource.network.models.get_user_courses


import com.google.gson.annotations.SerializedName

data class GetUserCourseItem(
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
    val progress: Any?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("status_progress")
    val statusProgress: String?,
    @SerializedName("title")
    val title: String?
)