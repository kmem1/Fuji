package com.clownteam.course_datasource.network.models.get_courses_modules


import com.google.gson.annotations.SerializedName

data class CourseModulesResponseItem(
    @SerializedName("count_lesson")
    val countLesson: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("is_complete")
    val isComplete: Boolean?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("progress")
    val progress: Int?,
    @SerializedName("title")
    val title: String?
)