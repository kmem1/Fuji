package com.clownteam.user_archive_datasource.models.get_courses

import com.google.gson.annotations.SerializedName

data class ArchiveCourseResultItem(
    @SerializedName("author")
    val author: AuthorModel?,
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
    val progress: ProgressModel?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("status_progress")
    val statusProgress: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("price")
    val price: Double?
)

data class AuthorModel(
    @SerializedName("path")
    val id: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?
)

data class ProgressModel(
    @SerializedName("progress")
    val progress: Int,
    @SerializedName("max_progress")
    val maxProgress: Int
)