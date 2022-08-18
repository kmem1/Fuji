package com.clownteam.course_datasource.network

import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.baseRequest
import com.clownteam.course_datasource.network.models.get_course_info.CourseInfoResponse
import com.clownteam.course_datasource.network.models.get_course_lessons.CourseLessonsResponse
import com.clownteam.course_datasource.network.models.get_course_step.CourseStepResponse
import com.clownteam.course_datasource.network.models.get_course_steps.CourseStepsResponse
import com.clownteam.course_datasource.network.models.get_courses.GetCoursesResponse
import com.clownteam.course_datasource.network.models.get_courses_modules.CourseModulesResponse
import com.clownteam.course_datasource.network.models.get_user_courses.GetUserCoursesResponse

@Suppress("BlockingMethodInNonBlockingContext")
class CourseServiceImpl(private val api: CourseApi) : CourseService {

    override suspend fun getCourses(token: String): NetworkResponse<GetCoursesResponse> =
        baseRequest { api.getCourses(token) }

    override suspend fun getUserCourses(
        token: String,
        userPath: String
    ): NetworkResponse<GetUserCoursesResponse> =
        baseRequest { api.getUserCourses(token, userPath) }

    override suspend fun startLearningCourse(
        token: String,
        courseId: String
    ): NetworkResponse<Any> = baseRequest { api.startLearningCourse(token, courseId) }

    override suspend fun getCourseInfo(
        token: String,
        id: String
    ): NetworkResponse<CourseInfoResponse> =
        baseRequest { api.getCourseInfo(token, id) }

    override suspend fun getCourseModules(
        token: String,
        courseId: String
    ): NetworkResponse<CourseModulesResponse> =
        baseRequest { api.getCourseModules(token, courseId) }

    override suspend fun getCourseLessons(
        token: String,
        courseId: String,
        moduleId: String
    ): NetworkResponse<CourseLessonsResponse> =
        baseRequest { api.getCourseLessons(token, courseId, moduleId) }

    override suspend fun getCourseSteps(
        token: String,
        courseId: String,
        moduleId: String,
        lessonId: String,
        stepId: String
    ): NetworkResponse<CourseStepsResponse> =
        baseRequest { api.getCourseSteps(token, courseId, moduleId, lessonId, stepId) }

    override suspend fun getCourseStep(
        token: String,
        courseId: String,
        moduleId: String,
        lessonId: String,
        stepId: String
    ): NetworkResponse<CourseStepResponse> =
        baseRequest { api.getCourseStep(token, courseId, moduleId, lessonId, stepId) }
}