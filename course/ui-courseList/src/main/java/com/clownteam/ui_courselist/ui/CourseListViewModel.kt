package com.clownteam.ui_courselist.ui

import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.DataState
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.ProgressBarState
import com.clownteam.core.domain.StateHolder
import com.clownteam.course_interactors.IGetMyCoursesUseCase
import com.clownteam.ui_courselist.test_data.TestData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val getMyCourses: IGetMyCoursesUseCase
) : ViewModel(), EventHandler<CourseListEvent>,
    StateHolder<MutableState<CourseListState>> {

    override val state: MutableState<CourseListState> = mutableStateOf(CourseListState())

    init {
        obtainEvent(CourseListEvent.GetCourses)
    }

    override fun obtainEvent(event: CourseListEvent) {
        when (event) {
            is CourseListEvent.GetCourses -> {
                getCourses()
            }
        }
    }

    private fun getCourses() {
        getMyCourses.invoke().onEach { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
                is DataState.Data -> {
                    state.value = state.value.copy(
                        myCourses = dataState.data ?: emptyList(),
                        popularCourses = TestData.testCourses
                    )
                }
                is DataState.Response -> {
                    state.value = state.value.copy(isError = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}