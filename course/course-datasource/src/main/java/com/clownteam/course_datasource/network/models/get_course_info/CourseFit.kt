package com.clownteam.course_datasource.network.models.get_course_info


import com.google.gson.annotations.SerializedName

data class CourseFit(
    @SerializedName("description")
    val description: String?,
    @SerializedName("title")
    val title: String?
)