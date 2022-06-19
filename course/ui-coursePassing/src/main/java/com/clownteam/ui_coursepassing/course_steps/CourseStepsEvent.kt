package com.clownteam.ui_coursepassing.course_steps

import com.clownteam.course_domain.CourseStep

sealed class CourseStepsEvent {

    object GetSteps : CourseStepsEvent()

    class UpdateCurrentStep(val step: CourseStep) : CourseStepsEvent()

    class GetStepContent(val stepId: String) : CourseStepsEvent()
}