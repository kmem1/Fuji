package com.clownteam.course_datasource.network.models.get_course_info


import com.google.gson.annotations.SerializedName

data class CourseRatingStars(
    @SerializedName("five")
    val five: Int?,
    @SerializedName("four")
    val four: Int?,
    @SerializedName("one")
    val one: Int?,
    @SerializedName("three")
    val three: Int?,
    @SerializedName("total_number")
    val totalNumber: Int?,
    @SerializedName("two")
    val two: Int?
)