package com.clownteam.course_datasource.network

import com.clownteam.core.network.NetworkResponse
import com.clownteam.course_datasource.network.models.get_course_info.CourseInfoResponse
import com.clownteam.course_datasource.network.models.get_courses.GetCoursesResponse
import com.clownteam.course_datasource.network.models.get_user_courses.GetUserCoursesResponse

interface CourseService {

    suspend fun getCourses(): NetworkResponse<GetCoursesResponse>

    suspend fun getUserCourses(
        token: String,
        username: String
    ): NetworkResponse<GetUserCoursesResponse>

    suspend fun getCourseInfo(token: String, id: String): NetworkResponse<CourseInfoResponse>
}