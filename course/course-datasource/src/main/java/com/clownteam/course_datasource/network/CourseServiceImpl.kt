package com.clownteam.course_datasource.network

import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.baseRequest
import com.clownteam.course_datasource.network.models.get_course_info.CourseInfoResponse
import com.clownteam.course_datasource.network.models.get_courses.GetCoursesResponse
import com.clownteam.course_datasource.network.models.get_user_courses.GetUserCoursesResponse

@Suppress("BlockingMethodInNonBlockingContext")
class CourseServiceImpl(private val api: CourseApi) : CourseService {

    override suspend fun getCourses(): NetworkResponse<GetCoursesResponse> =
        baseRequest { api.getCourses() }

    override suspend fun getUserCourses(
        token: String,
        username: String
    ): NetworkResponse<GetUserCoursesResponse> =
        baseRequest { api.getUserCourses("Bearer $token", username) }

    override suspend fun getCourseInfo(
        token: String,
        id: String
    ): NetworkResponse<CourseInfoResponse> =
        baseRequest { api.getCourseInfo("Bearer $token", id) }
}