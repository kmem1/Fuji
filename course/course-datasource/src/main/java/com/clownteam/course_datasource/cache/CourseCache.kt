package com.clownteam.course_datasource.cache

import com.clownteam.course_domain.Course
import com.clownteam.course_domain.CourseInfo

interface CourseCache {

    suspend fun getPopularCourses(): List<Course>

    suspend fun getMyCourses(): List<Course>

    suspend fun getCourse(id: Int): Course?

    suspend fun getCourseInfo(courseId: Int): CourseInfo?

    companion object Factory {
        fun build(): CourseCache {
            return CourseCacheImpl()
        }
    }
}
