package com.clownteam.ui_profile.all_courses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.components.UiText
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.SResult
import com.clownteam.profile_interactors.GetProfileCoursesUseCaseResult
import com.clownteam.profile_interactors.IGetProfileCoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllProfileCoursesViewModel @Inject constructor(
    private val getProfileCoursesUseCase: IGetProfileCoursesUseCase,
): ViewModel(), EventHandler<AllProfileCoursesScreenEvent> {

    var state by mutableStateOf(AllProfileCoursesScreenState())

    init {
        obtainEvent(AllProfileCoursesScreenEvent.GetCourses)
    }

    override fun obtainEvent(event: AllProfileCoursesScreenEvent) {
        when(event) {
            AllProfileCoursesScreenEvent.GetCourses -> {
                getProfileCourses()
            }
        }
    }

    private fun getProfileCourses() {
        state = state.copy(isLoading = true)

        viewModelScope.launch {
            val result = getProfileCoursesUseCase.invoke()

            handleGetProfileCoursesUseCaseResult(result)
        }
    }

    private fun handleGetProfileCoursesUseCaseResult(result: GetProfileCoursesUseCaseResult) {
        when (result) {
            GetProfileCoursesUseCaseResult.Failed -> {
                state = state.copy(errorMessage = UiText.DynamicString("Server Error"))
            }

            GetProfileCoursesUseCaseResult.NetworkError -> {
                state = state.copy(errorMessage = UiText.DynamicString("Network Error"))
            }

            is GetProfileCoursesUseCaseResult.Success -> {
                state = state.copy(courses = result.data)
            }

            GetProfileCoursesUseCaseResult.Unauthorized -> {
                state = state.copy(isUnauthorized = true)
            }
        }

        state = state.copy(isLoading = false)
    }
}