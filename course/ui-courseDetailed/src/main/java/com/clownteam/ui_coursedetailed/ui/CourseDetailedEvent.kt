package com.clownteam.ui_coursedetailed.ui

sealed class CourseDetailedEvent {

    object GetCourseInfo: CourseDetailedEvent()

    object StartLearning: CourseDetailedEvent()

    object ContinueLearning: CourseDetailedEvent()
}