package com.clownteam.course_datasource.cache

import com.clownteam.course_domain.Course

interface CourseCache {

    suspend fun getPopularCourses(): List<Course>

    suspend fun getMyCourses(): List<Course>

    companion object Factory {
        fun build(): CourseCache {
            return CourseCacheImpl()
        }
    }
}
