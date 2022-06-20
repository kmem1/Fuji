package com.clownteam.ui_coursepassing.course_steps

import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.course_domain.CourseStep
import com.clownteam.course_interactors.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class CourseStepsViewModel @Inject constructor(
    private val getCourseSteps: IGetCourseStepsUseCase,
    private val getCourseStep: IGetCourseStepUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), EventHandler<CourseStepsEvent>,
    StateHolder<MutableState<CourseStepsState>> {

    companion object {
        const val STEPS_ARG_KEY = "courseSteps_ARG_KEY"
    }

    override val state: MutableState<CourseStepsState> = mutableStateOf(CourseStepsState.Loading)

    private val courseId = savedStateHandle.get<CourseStepsArgs>(STEPS_ARG_KEY)?.courseId
    private val moduleId = savedStateHandle.get<CourseStepsArgs>(STEPS_ARG_KEY)?.moduleId
    private val lessonId = savedStateHandle.get<CourseStepsArgs>(STEPS_ARG_KEY)?.lessonId
    private val lessonTitle = savedStateHandle.get<CourseStepsArgs>(STEPS_ARG_KEY)?.lessonTitle
    private val stepId = savedStateHandle.get<CourseStepsArgs>(STEPS_ARG_KEY)?.stepId

    private val stepList = ArrayList<CourseStep>()
    private var currentStep: CourseStep? = null

    init {
        obtainEvent(CourseStepsEvent.GetSteps)
    }

    override fun obtainEvent(event: CourseStepsEvent) {
        when (event) {
            CourseStepsEvent.GetSteps -> {
                getSteps()
            }

            is CourseStepsEvent.UpdateCurrentStep -> {
                currentStep = event.step
                state.value.let {
                    if (it is CourseStepsState.CurrentStepLoading) {
                        val currentStepIndex = it.steps.indexOf(currentStep)
                        updateState(it.copy(currentStepIndex = currentStepIndex))
                    }

                    if (it is CourseStepsState.Data) {
                        val currentStepIndex = it.steps.indexOf(currentStep)
                        updateState(it.copy(currentStepIndex = currentStepIndex))
                    }
                }

                obtainEvent(CourseStepsEvent.GetStepContent(event.step.id))
            }

            is CourseStepsEvent.GetStepContent -> {
                getStepContent(event.stepId)
            }
        }
    }

    private fun getSteps() {
        viewModelScope.launch {
            updateState(CourseStepsState.Loading)

            if (courseId == null || moduleId == null || lessonId == null || stepId == null) {
                updateState(CourseStepsState.Error)
                return@launch
            }

            val params = GetCourseStepsParams(courseId, moduleId, lessonId, stepId)
            val modulesResult = getCourseSteps.invoke(params)

            handleGetCourseStepsResult(modulesResult)
        }
    }

    private fun getStepContent(stepId: String) {
        viewModelScope.launch {
            if (courseId == null || moduleId == null || lessonId == null || lessonTitle == null) {
                updateState(CourseStepsState.Error)
                return@launch
            }

            state.value.let {
                if (it is CourseStepsState.CurrentStepLoading) {
                    val currentStepIndex = it.steps.indexOfFirst { step -> step.id == stepId }
                    updateState(it.copy(currentStepIndex = currentStepIndex))
                }
            }

            val params = GetCourseStepParams(courseId, moduleId, lessonId, stepId)
            val result = getCourseStep.invoke(params)

            handleGetCourseStepResult(result)
        }
    }

    private fun handleGetCourseStepsResult(result: GetCourseStepsUseCaseResult) {
        when (result) {
            GetCourseStepsUseCaseResult.Failed -> {
                updateState(CourseStepsState.Error)
            }

            GetCourseStepsUseCaseResult.NetworkError -> {
                updateState(CourseStepsState.NetworkError)
            }

            is GetCourseStepsUseCaseResult.Success -> {
                val currentStepIndex = result.data.indexOfFirst { it.id == stepId }
                currentStep = result.data[currentStepIndex]

                currentStep?.let {
                    obtainEvent(CourseStepsEvent.GetStepContent(it.id))
                }

                stepList.clear()
                stepList.addAll(result.data)

                updateState(
                    CourseStepsState.CurrentStepLoading(
                        lessonTitle ?: "",
                        result.data,
                        currentStepIndex
                    )
                )
            }

            GetCourseStepsUseCaseResult.Unauthorized -> {
                updateState(CourseStepsState.Unauthorized)
            }
        }
    }

    private fun handleGetCourseStepResult(result: GetCourseStepUseCaseResult) {
        when (result) {
            GetCourseStepUseCaseResult.Failed -> {
                updateState(CourseStepsState.Error)
            }

            GetCourseStepUseCaseResult.NetworkError -> {
                updateState(CourseStepsState.NetworkError)
            }

            is GetCourseStepUseCaseResult.Success -> {
                currentStep = result.data
                val currentStepIndex = stepList.indexOfFirst { it.id == result.data.id }

                currentStep?.let {
                    stepList[currentStepIndex] = it
                }

                updateState(
                    CourseStepsState.Data(
                        lessonTitle = lessonTitle ?: "",
                        steps = stepList,
                        currentStepIndex = currentStepIndex,
                        currentStepContent = result.data.content
                    )
                )
            }

            GetCourseStepUseCaseResult.Unauthorized -> {
                updateState(CourseStepsState.Unauthorized)
            }
        }
    }

    @Parcelize
    data class CourseStepsArgs(
        val courseId: String,
        val moduleId: String,
        val lessonId: String,
        val lessonTitle: String,
        val stepId: String
    ) : Parcelable

    private fun updateState(newState: CourseStepsState) {
        state.value = newState
    }
}