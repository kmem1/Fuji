package com.clownteam.course_datasource.network

import com.clownteam.core.network.NetworkResponse
import com.clownteam.course_datasource.network.models.get_courses.GetCoursesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("BlockingMethodInNonBlockingContext")
class CourseServiceImpl(private val api: CourseApi) : CourseService {

    override suspend fun getCourses(): NetworkResponse<GetCoursesResponse> =
        withContext(Dispatchers.IO) {
            try {
                val apiResponse = api.getCourses().execute()

                NetworkResponse(statusCode = apiResponse.code(), data = apiResponse.body())
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResponse(isNetworkError = true, statusCode = 0, data = null)
            }
        }
}