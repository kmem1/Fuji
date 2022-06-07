package com.clownteam.ui_courselist.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.course_interactors.GetMyCoursesUseCaseResult
import com.clownteam.course_interactors.GetPopularCoursesUseCaseResult
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

            handleMyCoursesResult(myCoursesResult)
            handleGetCoursesResult(popularCoursesResult)
        }
    }

    private fun handleGetCoursesResult(result: GetPopularCoursesUseCaseResult) {
        when (result) {
            GetPopularCoursesUseCaseResult.Failed -> {
                state.value = CourseListState.Error(message = "Ошибка при получении данных")
            }

            GetPopularCoursesUseCaseResult.NetworkError -> {
                state.value = CourseListState.Error(message = "Проблемы с интернет соединением")
            }

            GetPopularCoursesUseCaseResult.Unauthorized -> {
                state.value = CourseListState.Error(message = "Необходима авторизация")
            }

            is GetPopularCoursesUseCaseResult.Success -> {
                val myCourses = (state.value as? CourseListState.Data)?.myCourses ?: emptyList()
                state.value = CourseListState.Data(
                    myCourses = myCourses,
                    popularCourses = result.data
                )
            }
        }
    }

    private fun handleMyCoursesResult(result: GetMyCoursesUseCaseResult) {
        when (result) {
            GetMyCoursesUseCaseResult.Failed -> {
                state.value = CourseListState.Error(message = "Error while retrieving data")
            }

            GetMyCoursesUseCaseResult.NetworkError -> {
                state.value = CourseListState.Error(message = "Network error")
            }

            is GetMyCoursesUseCaseResult.Success -> {
                val popularCourses =
                    (state.value as? CourseListState.Data)?.popularCourses ?: emptyList()
                state.value = CourseListState.Data(
                    myCourses = result.data,
                    popularCourses = popularCourses
                )
            }

            GetMyCoursesUseCaseResult.Unauthorized -> {
                state.value = CourseListState.Error(message = "Необходима авторизация")
            }
        }
    }
}
