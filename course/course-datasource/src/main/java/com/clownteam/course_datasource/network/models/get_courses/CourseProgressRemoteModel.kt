package com.clownteam.course_datasource.network.models.get_courses

import com.google.gson.annotations.SerializedName

data class CourseProgressRemoteModel(
    @SerializedName("max_progress")
    val maxProgress: Int?,
    @SerializedName("progress")
    val progress: Int?
)