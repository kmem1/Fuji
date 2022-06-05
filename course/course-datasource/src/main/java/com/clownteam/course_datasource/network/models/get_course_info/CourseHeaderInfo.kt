package com.clownteam.course_datasource.network.models.get_course_info

import com.google.gson.annotations.SerializedName

data class CourseHeaderInfo(
    @SerializedName("author")
    val author: String?,
    @SerializedName("duration_in_minutes")
    val durationInMinutes: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("members_amount")
    val membersAmount: Int?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("status_progress")
    val statusProgress: String?,
    @SerializedName("title")
    val title: String?
)