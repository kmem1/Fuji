package com.clownteam.course_datasource.network

import com.clownteam.core.network.NetworkResponse
import com.clownteam.course_datasource.network.models.get_courses.GetCoursesResponse

interface CourseService {

    suspend fun getCourses(): NetworkResponse<GetCoursesResponse>
}