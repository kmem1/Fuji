package com.clownteam.collection_datasource.models


import com.google.gson.annotations.SerializedName

open class CollectionCourseResponseItem {
    @SerializedName("author")
    val author: AuthorModel? = null

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