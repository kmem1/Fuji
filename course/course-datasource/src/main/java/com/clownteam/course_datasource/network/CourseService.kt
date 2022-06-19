package com.clownteam.course_datasource.network

import com.clownteam.core.network.NetworkResponse
import com.clownteam.course_datasource.network.models.get_course_info.CourseInfoResponse
import com.clownteam.course_datasource.network.models.get_course_lessons.CourseLessonsResponse
import com.clownteam.course_datasource.network.models.get_course_step.CourseStepResponse
import com.clownteam.course_datasource.network.models.get_course_steps.CourseStepsResponse
import com.clownteam.course_datasource.network.models.get_courses.GetCoursesResponse
import com.clownteam.course_datasource.network.models.get_courses_modules.CourseModulesResponse
import com.clownteam.course_datasource.network.models.get_user_courses.GetUserCoursesResponse
import retrofit2.http.Header
import retrofit2.http.Path

interface CourseService {

    suspend fun getCourses(token: String): NetworkResponse<GetCoursesResponse>

    suspend fun getUserCourses(
        token: String,
        userPath: String
    ): NetworkResponse<GetUserCoursesResponse>

    suspend fun getCourseInfo(token: String, id: String): NetworkResponse<CourseInfoResponse>

    suspend fun getCourseModules(
        token: String,
        courseId: String
    ): NetworkResponse<CourseModulesResponse>

    suspend fun getCourseLessons(
        token: String,
        courseId: String,
        moduleId: String
    ): NetworkResponse<CourseLessonsResponse>

    suspend fun getCourseSteps(
        token: String,
        courseId: String,
        moduleId: String,
        lessonId: String,
        stepId: String
    ): NetworkResponse<CourseStepsResponse>

    suspend fun getCourseStep(
        token: String,
        courseId: String,
        moduleId: String,
        lessonId: String,
        stepId: String
    ): NetworkResponse<CourseStepResponse>
}