package com.clownteam.collection_datasource.models.get_collection


import com.google.gson.annotations.SerializedName

data class GetCollectionResponseItem(
    @SerializedName("added_number")
    val addedNumber: Int?,
    @SerializedName("author")
    val author: String?,
    @SerializedName("courses")
    val courses: List<CollectionCourseResponseItem>?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("is_added")
    val isAdded: Boolean?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("title")
    val title: String?
)