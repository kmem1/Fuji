package com.clownteam.course_datasource.network.models.get_course_info


import com.google.gson.annotations.SerializedName

data class MainInfo(
    @SerializedName("goal_description")
    val goalDescription: String?,
    @SerializedName("title_image_url")
    val titleImageUrl: String?
)