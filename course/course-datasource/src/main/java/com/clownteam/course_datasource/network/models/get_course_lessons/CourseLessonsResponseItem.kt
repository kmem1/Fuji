package com.clownteam.course_datasource.network.models.get_course_lessons


import com.clownteam.course_datasource.network.models.get_courses_modules.ProgressModel
import com.google.gson.annotations.SerializedName

data class CourseLessonsResponseItem(
    @SerializedName("count_step")
    val countStep: Int?,
    @SerializedName("current_step")
    val currentStep: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("is_complete")
    val isComplete: Boolean?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("progress")
    val progress: ProgressModel?,
    @SerializedName("title")
    val title: String?
)