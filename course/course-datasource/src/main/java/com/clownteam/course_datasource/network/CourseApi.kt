package com.clownteam.course_datasource.network

import com.clownteam.course_datasource.network.models.get_course_info.CourseInfoResponse
import com.clownteam.course_datasource.network.models.get_course_lessons.CourseLessonsResponse
import com.clownteam.course_datasource.network.models.get_course_steps.CourseStepsResponse
import com.clownteam.course_datasource.network.models.get_courses.GetCoursesResponse
import com.clownteam.course_datasource.network.models.get_courses_modules.CourseModulesResponse
import com.clownteam.course_datasource.network.models.get_user_courses.GetUserCoursesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * Данный интерфейс описывает способы подключения к удаленному серверу через REST API
 */
interface CourseApi {

    @GET("api/courses/")
    fun getCourses(@Header("Authorization") token: String): Call<GetCoursesResponse>

    @GET("api/courses/all/{user_path}")
    fun getUserCourses(
        @Header("Authorization") token: String,
        @Path("user_path") userPath: String
    ): Call<GetUserCoursesResponse>

    @GET("api/courses/page/{id}")
    fun getCourseInfo(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<CourseInfoResponse>

    @GET("api/courses/learn/{courseId}/themes/")
    fun getCourseModules(
        @Header("Authorization") token: String,
        @Path("courseId") courseId: String
    ): Call<CourseModulesResponse>

    @GET("api/courses/learn/{courseId}/themes/{themeId}/lessons/")
    fun getCourseLessons(
        @Header("Authorization") token: String,
        @Path("courseId") courseId: String,
        @Path("themeId") themeId: String
    ): Call<CourseLessonsResponse>

    @GET("api/courses/learn/{courseId}/themes/{themeId}/lessons/{lessonId}/steps/{stepId}/list")
    fun getCourseSteps(
        @Header("Authorization") token: String,
        @Path("courseId") courseId: String,
        @Path("themeId") themeId: String,
        @Path("themeId") lessonId: String,
        @Path("stepId") stepId: String
    ): Call<CourseStepsResponse>
}