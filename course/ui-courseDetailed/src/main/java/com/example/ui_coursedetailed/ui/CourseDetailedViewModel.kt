package com.example.ui_coursedetailed.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.DataState
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.course_domain.CourseInfo
import com.clownteam.course_interactors.IGetCourseByIdUseCase
import com.clownteam.course_interactors.IGetCourseInfoByIdUseCase
import com.example.ui_coursedetailed.domain.toUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CourseDetailedViewModel @Inject constructor(
    private val getCourse: IGetCourseByIdUseCase,
    private val getCourseInfo: IGetCourseInfoByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), EventHandler<CourseDetailedEvent>, StateHolder<MutableState<CourseDetailedState>> {

    override val state: MutableState<CourseDetailedState> = mutableStateOf(CourseDetailedState())
    val courseId = savedStateHandle.get<Int>(COURSE_ID_ARG_KEY)

    init {
        if (courseId == null) {
            state.value = state.value.copy(isError = true)
        } else {
            obtainEvent(CourseDetailedEvent.GetCourseInfo)
        }
    }

    override fun obtainEvent(event: CourseDetailedEvent) {
        when (event) {
            is CourseDetailedEvent.GetCourseInfo -> {
                getCourse()
                getCourseInfo()
            }
        }
    }

    private fun getCourse() {
        courseId?.let { id ->
            getCourse.invoke(id).onEach { dataState ->
                when (dataState) {
                    is DataState.Loading -> {
                        if (dataState.progressBarState.isNotIdle()
                            || (dataState.progressBarState.isIdle() && state.value.hasAllData)
                        ) {
                            state.value =
                                state.value.copy(progressBarState = dataState.progressBarState)
                        }
                    }
                    is DataState.Data -> {
                        if (dataState.data == null) {
                            state.value = state.value.copy(
                                isError = true
                            )
                        } else {
                            state.value = state.value.copy(
                                course = dataState.data
                            )
                        }
                    }
                    is DataState.Response -> {
                        state.value = state.value.copy(isError = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getCourseInfo() {
        courseId?.let { id ->
            getCourseInfo.invoke(id).onEach { dataState ->
                when (dataState) {
                    is DataState.Loading -> {
                        if (dataState.progressBarState.isNotIdle()
                            || (dataState.progressBarState.isIdle() && state.value.hasAllData)
                        ) {
                            state.value =
                                state.value.copy(progressBarState = dataState.progressBarState)
                        }
                    }
                    is DataState.Data -> {
                        if (dataState.data == null) {
                            state.value = state.value.copy(
                                isError = true
                            )
                        } else {
                            state.value = state.value.copy(
                                courseInfo = dataState.data?.toUIModel()
                            )
                        }
                    }
                    is DataState.Response -> {
                        state.value = state.value.copy(isError = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    companion object {
        const val COURSE_ID_ARG_KEY = "courseId"
    }
}