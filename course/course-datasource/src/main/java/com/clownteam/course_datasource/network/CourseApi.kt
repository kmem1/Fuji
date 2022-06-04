package com.clownteam.course_datasource.network

import com.clownteam.course_datasource.network.models.get_courses.GetCoursesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CourseApi {

    @GET("api/courses/")
    fun getCourses(): Call<GetCoursesResponse>
}