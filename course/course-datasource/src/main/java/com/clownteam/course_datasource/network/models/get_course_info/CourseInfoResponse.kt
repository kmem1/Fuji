package com.clownteam.course_datasource.network.models.get_course_info


import com.google.gson.annotations.SerializedName

data class CourseInfoResponse(
    @SerializedName("course")
    val mainInfo: CourseHeaderInfo?,
    @SerializedName("info")
    val info: CourseInfo?
)