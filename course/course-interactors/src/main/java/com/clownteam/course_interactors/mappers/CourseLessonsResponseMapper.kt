package com.clownteam.course_interactors.mappers

import com.clownteam.course_datasource.network.models.get_course_lessons.CourseLessonsResponseItem
import com.clownteam.course_domain.CourseLesson

object CourseLessonsResponseMapper {

    fun map(input: CourseLessonsResponseItem): CourseLesson {
        return CourseLesson(
            id = input.path ?: "",
            title = input.title ?: "",
            currentProgress = input.progress?.currentProgress ?: 0,
            maxProgress = input.countStep ?: 0,
            currentStepId = getCurrentStepId(input.currentStep)
        )
    }

    private fun getCurrentStepId(path: String?): String {
        if (path == null) return ""

        val l = path.lastIndexOf("/")
        return path.substring((l + 1) until path.length)
    }
}