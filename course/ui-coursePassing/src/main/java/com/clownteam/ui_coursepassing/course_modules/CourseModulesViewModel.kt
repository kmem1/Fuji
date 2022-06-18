package com.clownteam.ui_coursepassing.course_modules

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.course_interactors.GetCourseInfoByIdUseCaseResult
import com.clownteam.course_interactors.GetCourseModulesUseCaseResult
import com.clownteam.course_interactors.IGetCourseInfoByIdUseCase
import com.clownteam.course_interactors.IGetCourseModulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseModulesViewModel @Inject constructor(
    private val getCourseModules: IGetCourseModulesUseCase,
    private val getCourseInfoByIdUseCaseResult: IGetCourseInfoByIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), EventHandler<CourseModulesEvent>,
    StateHolder<MutableState<CourseModulesState>> {

    companion object {
        const val COURSE_ID_ARG_KEY = "courseModules_COURSE_ID_KEY"
    }

    override val state: MutableState<CourseModulesState> =
        mutableStateOf(CourseModulesState.Loading)

    private val courseId = savedStateHandle.get<String>(COURSE_ID_ARG_KEY)

    init {
        obtainEvent(CourseModulesEvent.GetModules)
    }

    override fun obtainEvent(event: CourseModulesEvent) {
        when (event) {
            CourseModulesEvent.GetModules -> {
                getModules()
            }
        }
    }

    private fun getModules() {
        viewModelScope.launch {
            updateState(CourseModulesState.Loading)

            if (courseId == null) {
                updateState(CourseModulesState.Error)
                return@launch
            }

            val modulesResult = getCourseModules.invoke(courseId)
            val courseResult = getCourseInfoByIdUseCaseResult.invoke(courseId)

            handleGetCoursesModulesResult(modulesResult)
            handleCourseInfoUseCaseResult(courseResult)
        }
    }

    private fun handleGetCoursesModulesResult(result: GetCourseModulesUseCaseResult) {
        when (result) {
            GetCourseModulesUseCaseResult.Failed -> {
                updateState(CourseModulesState.Error)
            }

            GetCourseModulesUseCaseResult.NetworkError -> {
                updateState(CourseModulesState.NetworkError)
            }

            is GetCourseModulesUseCaseResult.Success -> {
                state.value.let {
                    val course = if (it is CourseModulesState.Data) it.course else null
                    updateState(CourseModulesState.Data(course, result.data))
                }
            }

            GetCourseModulesUseCaseResult.Unauthorized -> {
                updateState(CourseModulesState.Unauthorized)
            }
        }
    }

    private fun handleCourseInfoUseCaseResult(result: GetCourseInfoByIdUseCaseResult) {
        when (result) {
            GetCourseInfoByIdUseCaseResult.Failed -> {
                updateState(CourseModulesState.Error)
            }

            GetCourseInfoByIdUseCaseResult.NetworkError -> {
                updateState(CourseModulesState.Error)
            }

            is GetCourseInfoByIdUseCaseResult.Success -> {
                state.value.let {
                    val modules = if (it is CourseModulesState.Data) it.modules else emptyList()
                    updateState(CourseModulesState.Data(result.course, modules))
                }
            }

            GetCourseInfoByIdUseCaseResult.Unauthorized -> {
                updateState(CourseModulesState.Unauthorized)
            }
        }
    }

    private fun updateState(newState: CourseModulesState) {
        state.value = newState
    }
}