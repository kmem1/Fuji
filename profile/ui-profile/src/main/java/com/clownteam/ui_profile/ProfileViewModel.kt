package com.clownteam.ui_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.EventHandler
import com.clownteam.profile_interactors.GetProfileUseCaseResult
import com.clownteam.profile_interactors.IGetProfileUseCase
import com.clownteam.profile_interactors.ISignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: IGetProfileUseCase,
    private val signOut: ISignOutUseCase
) : ViewModel(), EventHandler<ProfileEvent> {

    var state by mutableStateOf(ProfileState())

    private val eventChannel = Channel<ProfileViewModelEvent>()
    val events = eventChannel.receiveAsFlow()

    override fun obtainEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.SignOut -> {
                viewModelScope.launch {
                    signOut.invoke()

                    eventChannel.send(ProfileViewModelEvent.NavigateToLoginEvent)
                }
            }

            is ProfileEvent.GetProfile -> {
                getProfile()
            }
        }
    }

    private fun getProfile() {
        state = state.copy(isLoading = true, isNetworkError = false)

        viewModelScope.launch {

            val result = getProfileUseCase.invoke()

            handleProfileUseCaseResult(result)
        }

        state = state.copy(isLoading = false)
    }

    private fun handleProfileUseCaseResult(result: GetProfileUseCaseResult) {
        when (result) {
            GetProfileUseCaseResult.Failed -> {
                state = state.copy(isNetworkError = true)
            }

            GetProfileUseCaseResult.NetworkError -> {
                state = state.copy(isNetworkError = true)
            }

            is GetProfileUseCaseResult.Success -> {
                state = state.copy(
                    isNetworkError = false,
                    username = result.data.username,
                    avatarUrl = result.data.avatarUrl
                )
            }

            GetProfileUseCaseResult.Unauthorized -> {
                viewModelScope.launch {
                    eventChannel.send(ProfileViewModelEvent.UnauthorizedEvent)
                }
            }
        }
    }

    sealed class ProfileViewModelEvent {
        object UnauthorizedEvent : ProfileViewModelEvent()

        object NavigateToLoginEvent : ProfileViewModelEvent()
    }
}