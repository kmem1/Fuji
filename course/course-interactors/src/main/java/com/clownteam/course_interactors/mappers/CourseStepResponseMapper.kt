package com.clownteam.course_interactors.mappers

import com.clownteam.course_datasource.network.models.get_course_step.CourseStepResponse
import com.clownteam.course_datasource.network.models.get_course_steps.CourseStepsResponseItem
import com.clownteam.course_domain.CourseStep

object CourseStepResponseMapper {

    fun map(input: CourseStepResponse): CourseStep {
        return CourseStep(
            id = input.path ?: "",
            isComplete = input.isComplete ?: false,
            content = input.content ?: ""
        )
    }
}