package com.clownteam.ui_coursedetailed.ui

sealed class CourseDetailedEvent {

    object GetCourseInfo: CourseDetailedEvent()

    object LearnCourse: CourseDetailedEvent()

    object LearningStarted: CourseDetailedEvent()

}