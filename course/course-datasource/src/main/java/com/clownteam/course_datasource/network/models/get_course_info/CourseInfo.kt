package com.clownteam.course_datasource.network.models.get_course_info

import com.google.gson.annotations.SerializedName

data class CourseInfo(
    @SerializedName("fits")
    val fits: List<CourseFit>?,
    @SerializedName("main_info")
    val mainInfo: MainInfo?,
    @SerializedName("skills")
    val skills: List<String>?,
    @SerializedName("stars")
    val stars: CourseRatingStars?
)