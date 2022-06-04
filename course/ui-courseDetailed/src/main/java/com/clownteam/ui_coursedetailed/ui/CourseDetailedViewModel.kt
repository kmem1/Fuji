package com.clownteam.ui_coursedetailed.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.*
import com.clownteam.course_interactors.IGetCourseByIdUseCase
import com.clownteam.course_interactors.IGetCourseInfoByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailedViewModel @Inject constructor(
    private val getCourse: IGetCourseByIdUseCase,
    private val getCourseInfo: IGetCourseInfoByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), EventHandler<CourseDetailedEvent>, StateHolder<MutableState<CourseDetailedState>> {

    constructor(
        getCourse: IGetCourseByIdUseCase,
        getCourseInfo: IGetCourseInfoByIdUseCase,
        savedStateHandle: SavedStateHandle,
        listener: Listener
    ) : this(getCourse, getCourseInfo, savedStateHandle) {
        this.listener = listener
    }

    interface Listener {
        fun onStateChanged(newState: CourseDetailedState)
    }

    override val state: MutableState<CourseDetailedState> =
        mutableStateOf(CourseDetailedState.Loading)
    private val courseId = savedStateHandle.get<String>(COURSE_ID_ARG_KEY)

    private var listener: Listener? = null

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

            val courseResult = getCourse.invoke(courseId)
            val courseInfoResult = getCourseInfo.invoke(courseId)

            if (courseResult is SResult.Success && courseInfoResult is SResult.Success) {
                updateState(
                    CourseDetailedState.Data(
                        course = courseResult.data,
                        courseInfo = courseInfoResult.data
                    )
                )
            } else {
                updateState(CourseDetailedState.Error)
            }
        }
    }

    private fun updateState(newState: CourseDetailedState) {
        state.value = newState
        listener?.onStateChanged(newState)
    }

    companion object {
        const val COURSE_ID_ARG_KEY = "courseId"
    }
}