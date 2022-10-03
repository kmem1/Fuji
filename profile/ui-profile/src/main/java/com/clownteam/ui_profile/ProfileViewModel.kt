package com.clownteam.ui_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.SResult
import com.clownteam.profile_interactors.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: IGetProfileUseCase,
    private val getProfileCoursesUseCase: IGetProfileCoursesUseCase,
    private val getProfileCollectionsUseCase: IGetProfileCollectionsUseCase,
    private val signOut: ISignOutUseCase
) : ViewModel(), EventHandler<ProfileEvent> {

    var state by mutableStateOf(ProfileState())

    init {
        obtainEvent(ProfileEvent.GetProfile)
        obtainEvent(ProfileEvent.GetProfileCourses)
        obtainEvent(ProfileEvent.GetProfileCollections)
    }

    override fun obtainEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.SignOut -> {
                signOut()
            }

            is ProfileEvent.GetProfile -> {
                getProfile()
            }

            is ProfileEvent.GetProfileCourses -> {
                getProfileCourses()
            }

            ProfileEvent.GetProfileCollections -> {
                getProfileCollections()
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            signOut.invoke()
            state = state.copy(profileData = SResult.Empty)
        }
    }

    private fun getProfile() {
        state = state.copy(profileData = SResult.Loading(), isNetworkError = false)

        viewModelScope.launch {
            val result = getProfileUseCase.invoke()

            handleProfileUseCaseResult(result)
        }
    }

    private fun handleProfileUseCaseResult(result: GetProfileUseCaseResult) {
        when (result) {
            GetProfileUseCaseResult.Failed -> {
                state = state.copy(profileData = SResult.Failed())
            }

            GetProfileUseCaseResult.NetworkError -> {
                state = state.copy(isNetworkError = true)
            }

            is GetProfileUseCaseResult.Success -> {
                state = state.copy(
                    isNetworkError = false,
                    profileData = SResult.Success(result.data)
                )
            }

            GetProfileUseCaseResult.Unauthorized -> {
                state = state.copy(
                    isNetworkError = false,
                    isUnauthorized = true
                )
            }
        }
    }

    private fun getProfileCourses() {
        state = state.copy(profileCourses = SResult.Loading(), isNetworkError = false)

        viewModelScope.launch {
            val result = getProfileCoursesUseCase.invoke()

            handleGetProfileCoursesUseCaseResult(result)
        }
    }

    private fun handleGetProfileCoursesUseCaseResult(result: GetProfileCoursesUseCaseResult) {
        when (result) {
            GetProfileCoursesUseCaseResult.Failed -> {
                state = state.copy(profileCourses = SResult.Failed("Server Error"))
            }

            GetProfileCoursesUseCaseResult.NetworkError -> {
                state = state.copy(profileCourses = SResult.Failed("Network Error"))
            }

            is GetProfileCoursesUseCaseResult.Success -> {
                state = state.copy(
                    isNetworkError = false,
                    profileCourses = SResult.Success(result.data)
                )
            }

            GetProfileCoursesUseCaseResult.Unauthorized -> {
                state = state.copy(
                    isNetworkError = false,
                    isUnauthorized = true
                )
            }
        }
    }

    private fun getProfileCollections() {
        state = state.copy(profileCourses = SResult.Loading(), isNetworkError = false)

        viewModelScope.launch {
            val result = getProfileCollectionsUseCase.invoke()

            handleGetProfileCollectionsUseCaseResult(result)
        }
    }

    private fun handleGetProfileCollectionsUseCaseResult(result: GetProfileCollectionsUseCaseResult) {
        when (result) {
            GetProfileCollectionsUseCaseResult.Failed -> {
                state = state.copy(profileCollections = SResult.Failed("Server Error"))
            }

            GetProfileCollectionsUseCaseResult.NetworkError -> {
                state = state.copy(profileCollections = SResult.Failed("Network Error"))
            }

            is GetProfileCollectionsUseCaseResult.Success -> {
                state = state.copy(
                    isNetworkError = false,
                    profileCollections = SResult.Success(result.data)
                )
            }

            GetProfileCollectionsUseCaseResult.Unauthorized -> {
                state = state.copy(
                    isNetworkError = false,
                    isUnauthorized = true
                )
            }
        }
    }
}