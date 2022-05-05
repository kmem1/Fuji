package com.clownteam.ui_courselist.ui

import com.clownteam.course_domain.Course

sealed class CourseListState {

    object Loading : CourseListState()

    class Data(val myCourses: List<Course>, val popularCourses: List<Course>) : CourseListState()

    class Error(val message: String) : CourseListState()
}