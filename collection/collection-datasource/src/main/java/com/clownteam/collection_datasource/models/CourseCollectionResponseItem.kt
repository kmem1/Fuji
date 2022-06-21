package com.clownteam.collection_datasource.models


import com.google.gson.annotations.SerializedName

open class CourseCollectionResponseItem {
    @SerializedName("added_number")
    val addedNumber: Int? = null

    @SerializedName("author")
    val author: CourseCollectionsAuthorResponseItem? = null

    @SerializedName("courses")
    val courses: List<CollectionCourseResponseItem>? = null

    @SerializedName("image_url")
    val imageUrl: String? = null

    @SerializedName("is_added")
    val isAdded: Boolean? = null

    @SerializedName("path")
    val path: String? = null

    @SerializedName("rating")
    val rating: Double? = null

    @SerializedName("title")
    val title: String? = null
}

