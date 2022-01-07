package com.clownteam.ui_courselist.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.DataState
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.course_interactors.IGetMyCoursesUseCase
import com.clownteam.course_interactors.IGetPopularCoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val getMyCourses: IGetMyCoursesUseCase,
    private val getPopularCourses: IGetPopularCoursesUseCase
) : ViewModel(), EventHandler<CourseListEvent>,
    StateHolder<MutableState<CourseListState>> {

    override val state: MutableState<CourseListState> = mutableStateOf(CourseListState())

    init {
        obtainEvent(CourseListEvent.GetCourses)
    }

    override fun obtainEvent(event: CourseListEvent) {
        when (event) {
            is CourseListEvent.GetCourses -> {
                getMyCourses()
                getPopularCourses()
            }
        }
    }

    private fun getMyCourses() {
        getMyCourses.invoke().onEach { dataState ->
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
                    state.value = state.value.copy(
                        myCourses = dataState.data ?: emptyList()
                    )
                }
                is DataState.Response -> {
                    state.value = state.value.copy(isError = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getPopularCourses() {
        getPopularCourses.invoke().onEach { dataState ->
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
                    state.value = state.value.copy(
                        popularCourses = dataState.data ?: emptyList()
                    )
                }
                is DataState.Response -> {
                    state.value = state.value.copy(isError = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
