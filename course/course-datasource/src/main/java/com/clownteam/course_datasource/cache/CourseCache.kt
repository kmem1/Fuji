package com.clownteam.course_datasource.cache

import com.clownteam.core.domain.SResult
import com.clownteam.course_domain.Course
import com.clownteam.course_domain.CourseInfo

interface CourseCache {

    suspend fun getPopularCourses(): SResult<List<Course>>

    suspend fun getMyCourses(): SResult<List<Course>>

    suspend fun getCourse(id: String): SResult<Course>

    suspend fun getCourseInfo(courseId: String): SResult<CourseInfo>

    companion object Factory {
        fun build(): CourseCache {
            return CourseCacheImpl()
        }
    }
}
