package com.clownteam.course_interactors.mappers

import com.clownteam.course_datasource.network.models.get_courses_modules.CourseModulesResponseItem
import com.clownteam.course_domain.CourseModule

object CourseModulesResponseMapper {

    fun map(input: CourseModulesResponseItem): CourseModule {
        return CourseModule(
            id = input.path ?: "",
            title = input.title ?: "",
            currentProgress = input.progress ?: 0,
            maxProgress = input.countLesson ?: 0
        )
    }
}