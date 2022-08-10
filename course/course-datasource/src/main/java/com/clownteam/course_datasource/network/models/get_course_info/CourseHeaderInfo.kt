package com.clownteam.course_datasource.network.models.get_course_info

import com.google.gson.annotations.SerializedName

data class CourseHeaderInfo(
    @SerializedName("path")
    val id: String,
    @SerializedName("author")
    val author: AuthorModel?,
    @SerializedName("duration_in_minutes")
    val durationInMinutes: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("members_amount")
    val membersAmount: Int?,
    @SerializedName("rating")
    val rating: RatingModel?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("status_progress")
    val statusProgress: String?,
    @SerializedName("title")
    val title: String?
)

data class AuthorModel(
    @SerializedName("path")
    val id: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?
)

data class RatingModel(
    @SerializedName("value")
    val value: Double? = null,
    @SerializedName("reviews_count")
    val reviewCount: Int?
)