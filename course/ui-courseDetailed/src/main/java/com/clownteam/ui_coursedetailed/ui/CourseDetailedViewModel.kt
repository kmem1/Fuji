package com.clownteam.ui_coursedetailed.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.course_interactors.GetCourseInfoByIdUseCaseResult
import com.clownteam.course_interactors.IGetCourseInfoByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailedViewModel @Inject constructor(
    private val getCourseInfo: IGetCourseInfoByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), EventHandler<CourseDetailedEvent>, StateHolder<MutableState<CourseDetailedState>> {

    override val state: MutableState<CourseDetailedState> =
        mutableStateOf(CourseDetailedState.Loading)
    private val courseId = savedStateHandle.get<String>(COURSE_ID_ARG_KEY)


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
        }
    }

    private fun getCourseInfo() {
        viewModelScope.launch {
            if (courseId == null) {
                updateState(CourseDetailedState.Error)
                return@launch
            }

            updateState(CourseDetailedState.Loading)

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
