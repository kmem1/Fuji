package com.clownteam.course_interactors.mappers

import com.clownteam.course_datasource.network.models.get_course_lessons.CourseLessonsResponseItem
import com.clownteam.course_domain.CourseLesson

object CourseLessonsResponseMapper {

    fun map(input: CourseLessonsResponseItem): CourseLesson {
        return CourseLesson(
            id = input.path ?: "",
            title = input.title ?: "",
            currentProgress = input.progress ?: 0,
            maxProgress = input.countStep ?: 0
        )
    }
}