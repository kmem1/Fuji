package com.clownteam.ui_coursedetailed.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.course_domain.Course
import com.clownteam.course_domain.CourseInfoUI
import com.clownteam.course_interactors.GetCourseInfoByIdUseCaseResult
import com.clownteam.course_interactors.IGetCourseInfoByIdUseCase
import com.clownteam.course_interactors.IStartLearningCourseUseCase
import com.clownteam.course_interactors.StartLearningCourseUseCaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailedViewModel @Inject constructor(
    private val getCourseInfo: IGetCourseInfoByIdUseCase,
    private val startLearningCourse: IStartLearningCourseUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), EventHandler<CourseDetailedEvent>, StateHolder<MutableState<CourseDetailedState>> {

    override val state: MutableState<CourseDetailedState> =
        mutableStateOf(CourseDetailedState.Loading)
    private val courseId = savedStateHandle.get<String>(COURSE_ID_ARG_KEY)

    private var course: Course? = null
    private var courseInfo: CourseInfoUI? = null

    init {
        if (courseId == null) {
            updateState(CourseDetailedState.Error)
        } else {
            obtainEvent(CourseDetailedEvent.GetCourseInfo)
        }
    }

    override fun obtainEvent(event: CourseDetailedEvent) {
        when (event) {
            is CourseDetailedEvent.GetCourseInfo -> {
                getCourseInfo()
            }

            CourseDetailedEvent.LearningStarted -> {
                course?.let { _course ->
                    courseInfo?.let { _courseInfo ->
                        updateState(CourseDetailedState.Data(_course, _courseInfo, false))
                        if (course?.isStarted != true) {
                            getCourseInfo(showLoading = false)
                        }
                    }
                }
            }

            CourseDetailedEvent.LearnCourse -> {
                course?.let { _course ->
                    courseInfo?.let { _courseInfo ->
                        if (_course.isStarted) {
                            updateState(CourseDetailedState.Data(_course, _courseInfo, true))
                        } else {
                            startLearning()
                        }
                    }
                }
            }
        }
    }

    private fun startLearning() {
        viewModelScope.launch {
            val result = startLearningCourse.invoke(courseId ?: "")

            handleStartLearningUseCaseResult(result)
        }
    }

    private fun handleStartLearningUseCaseResult(result: StartLearningCourseUseCaseResult) {
        when (result) {
            StartLearningCourseUseCaseResult.Failed -> {}

            StartLearningCourseUseCaseResult.NetworkError -> {}

            StartLearningCourseUseCaseResult.Success -> {
                course?.let { _course ->
                    courseInfo?.let { _courseInfo ->
                        updateState(CourseDetailedState.Data(_course, _courseInfo, true))
                    }
                }
            }

            StartLearningCourseUseCaseResult.Unauthorized -> {}
        }
    }

    private fun getCourseInfo(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (courseId == null) {
                updateState(CourseDetailedState.Error)
                return@launch
            }

            if (showLoading) updateState(CourseDetailedState.Loading)

            val courseInfoResult = getCourseInfo.invoke(courseId)

            handleCourseInfoUseCaseResult(courseInfoResult)
        }
    }

    private fun handleCourseInfoUseCaseResult(result: GetCourseInfoByIdUseCaseResult) {
        when (result) {
            GetCourseInfoByIdUseCaseResult.Failed -> {
                updateState(CourseDetailedState.Error)
            }

            GetCourseInfoByIdUseCaseResult.NetworkError -> {
                updateState(CourseDetailedState.Error)
            }

            is GetCourseInfoByIdUseCaseResult.Success -> {
                course = result.course
                courseInfo = result.courseInfo
                updateState(CourseDetailedState.Data(result.course, result.courseInfo))
            }

            GetCourseInfoByIdUseCaseResult.Unauthorized -> {
                updateState(CourseDetailedState.Unauthorized)
            }
        }
    }

    private fun updateState(newState: CourseDetailedState) {
        state.value = newState
    }

    companion object {
        const val COURSE_ID_ARG_KEY = "courseId"
    }
}
