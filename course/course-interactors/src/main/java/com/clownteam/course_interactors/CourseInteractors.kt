package com.clownteam.course_interactors

class CourseInteractors private constructor(
    val getMyCourses: IGetMyCoursesUseCase
) {
    companion object Factory {
        fun build(): CourseInteractors {
            return CourseInteractors(GetMyCoursesUseCase())
        }
    }
}