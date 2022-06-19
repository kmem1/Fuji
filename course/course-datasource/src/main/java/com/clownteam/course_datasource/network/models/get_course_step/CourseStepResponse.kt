package com.clownteam.course_datasource.network.models.get_course_step


import com.google.gson.annotations.SerializedName

data class CourseStepResponse(
    @SerializedName("content")
    val content: String?,
    @SerializedName("is_complete")
    val isComplete: Boolean?,
    @SerializedName("next")
    val next: String?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("prev")
    val prev: String?,
    @SerializedName("title")
    val title: String?
)