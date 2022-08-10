package com.clownteam.ui_coursepassing.course_lessons

import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.course_interactors.GetCourseLessonsParams
import com.clownteam.course_interactors.GetCourseLessonsUseCaseResult
import com.clownteam.course_interactors.IGetCourseLessonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
class CourseLessonsViewModel @Inject constructor(
    private val getCourseLessons: IGetCourseLessonsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), EventHandler<CourseLessonsEvent>,
    StateHolder<MutableState<CourseLessonsState>> {

    companion object {
        const val LESSONS_ARG_KEY = "courseLessons_ARG_KEY"
    }

    override val state: MutableState<CourseLessonsState> =
        mutableStateOf(CourseLessonsState.Loading)

    private val courseId = savedStateHandle.get<CourseLessonsArgs>(LESSONS_ARG_KEY)?.courseId
    private val moduleId = savedStateHandle.get<CourseLessonsArgs>(LESSONS_ARG_KEY)?.moduleId
    private val moduleName = savedStateHandle.get<CourseLessonsArgs>(LESSONS_ARG_KEY)?.moduleName

    init {
        obtainEvent(CourseLessonsEvent.GetLessons)
    }

    override fun obtainEvent(event: CourseLessonsEvent) {
        when (event) {
            CourseLessonsEvent.GetLessons -> {
                getLessons()
            }
        }
    }

    private fun getLessons() {
        viewModelScope.launch {
            updateState(CourseLessonsState.Loading)

            if (courseId == null || moduleId == null) {
                updateState(CourseLessonsState.Error)
                return@launch
            }

            val params = GetCourseLessonsParams(courseId, moduleId)
            val modulesResult = getCourseLessons.invoke(params)

            handleGetCourseLessonsResult(modulesResult)
        }
    }

    private fun handleGetCourseLessonsResult(result: GetCourseLessonsUseCaseResult) {
        when (result) {
            GetCourseLessonsUseCaseResult.Failed -> {
                updateState(CourseLessonsState.Error)
            }

            GetCourseLessonsUseCaseResult.NetworkError -> {
                updateState(CourseLessonsState.NetworkError)
            }

            is GetCourseLessonsUseCaseResult.Success -> {
                updateState(
                    CourseLessonsState.Data(
                        courseId = courseId ?: "",
                        moduleId = moduleId ?: "",
                        moduleName = moduleName ?: "",
                        lessons = result.data
                    )
                )
            }

            GetCourseLessonsUseCaseResult.Unauthorized -> {
                updateState(CourseLessonsState.Unauthorized)
            }
        }
    }

    @Parcelize
    data class CourseLessonsArgs(
        val courseId: String,
        val moduleId: String,
        val moduleName: String
    ) : Parcelable

    private fun updateState(newState: CourseLessonsState) {
        state.value = newState
    }
}