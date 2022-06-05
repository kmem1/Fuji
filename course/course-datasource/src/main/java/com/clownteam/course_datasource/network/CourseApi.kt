package com.clownteam.course_datasource.network

import com.clownteam.course_datasource.network.models.get_course_info.CourseInfoResponse
import com.clownteam.course_datasource.network.models.get_courses.GetCoursesResponse
import com.clownteam.course_datasource.network.models.get_user_courses.GetUserCoursesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CourseApi {

    @GET("api/courses/")
    fun getCourses(): Call<GetCoursesResponse>

    @GET("api/courses/all/{username}")
    fun getUserCourses(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Call<GetUserCoursesResponse>

    @GET("api/courses/page/{id}")
    fun getCourseInfo(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<CourseInfoResponse>
}