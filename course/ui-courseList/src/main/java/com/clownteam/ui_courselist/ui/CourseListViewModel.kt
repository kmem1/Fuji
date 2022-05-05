package com.clownteam.ui_courselist.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.SResult
import com.clownteam.core.domain.StateHolder
import com.clownteam.course_interactors.IGetMyCoursesUseCase
import com.clownteam.course_interactors.IGetPopularCoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val getMyCourses: IGetMyCoursesUseCase,
    private val getPopularCourses: IGetPopularCoursesUseCase
) : ViewModel(), EventHandler<CourseListEvent>,
    StateHolder<MutableState<CourseListState>> {

    override val state: MutableState<CourseListState> = mutableStateOf(CourseListState.Loading)

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
        viewModelScope.launch {
            state.value = CourseListState.Loading

            val myCoursesResult = getMyCourses.invoke()
            val popularCoursesResult = getPopularCourses.invoke()

            if (myCoursesResult is SResult.Success && popularCoursesResult is SResult.Success) {
                state.value = CourseListState.Data(
                    myCourses = myCoursesResult.data,
                    popularCourses = popularCoursesResult.data
                )
            } else {
                state.value = CourseListState.Error(message = "Error while retrieving data")
            }
        }
    }
}
