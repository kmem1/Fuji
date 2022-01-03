package com.clownteam.course_datasource.cache

import com.clownteam.course_domain.Course

interface CourseCache {

    fun getCurrentCourse(): Course

    fun getMyCourses(): List<Course>
}