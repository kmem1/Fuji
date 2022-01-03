package com.clownteam.ui_courselist.ui

sealed class CourseListEvent {

    object GetCourses: CourseListEvent()
}