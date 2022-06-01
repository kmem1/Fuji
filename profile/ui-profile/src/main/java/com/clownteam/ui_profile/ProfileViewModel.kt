package com.clownteam.ui_profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.network.TokenManager
import com.clownteam.profile_interactors.GetProfileUseCaseResult
import com.clownteam.profile_interactors.IGetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: IGetProfileUseCase,
    private val tokenManager: TokenManager
) : ViewModel(), EventHandler<ProfileEvent> {

    var state by mutableStateOf(ProfileState())

    private val eventChannel = Channel<ProfileViewModelEvent>()
    val events = eventChannel.receiveAsFlow()

    override fun obtainEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.SignOut -> {
                viewModelScope.launch {
                    tokenManager.clearTokens()

                    eventChannel.send(ProfileViewModelEvent.NavigateToLoginEvent)
                }
            }

            is ProfileEvent.GetProfile -> {
                if (event.accessToken == null) {
                    getProfile(tokenManager.getToken())
                } else {
                    getProfile(event.accessToken)
                }
            }
        }
    }

    private fun getProfile(token: String?) {
        state = state.copy(isLoading = true, isNetworkError = false)

        Log.d("Kmem", "token: $token")

        viewModelScope.launch {
            if (token == null) {
                eventChannel.send(ProfileViewModelEvent.UnauthorizedEvent)
                return@launch
            }

            val result = getProfileUseCase.invoke(token)

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

            GetProfileUseCaseResult.TokenExpired -> {
                tryToRefreshToken()
            }
        }
    }

    private fun tryToRefreshToken() {
        viewModelScope.launch {
            val newTokenResponse = tokenManager.refreshToken()

            if (newTokenResponse.isNetworkError) {
                state = state.copy(isNetworkError = true)
                return@launch
            }

            if (newTokenResponse.isSuccessCode && newTokenResponse.data != null) {
                getProfile(newTokenResponse.data)
            } else {
                eventChannel.send(ProfileViewModelEvent.UnauthorizedEvent)
            }
        }
    }

    sealed class ProfileViewModelEvent {
        object UnauthorizedEvent : ProfileViewModelEvent()

        object NavigateToLoginEvent : ProfileViewModelEvent()
    }
}