package com.clownteam.course_datasource.network.models.get_course_steps


import com.google.gson.annotations.SerializedName

data class CourseStepsResponseItem(
    @SerializedName("is_active")
    val isActive: Boolean?,
    @SerializedName("is_complete")
    val isComplete: Boolean?,
    @SerializedName("path")
    val path: String?
)