package com.clownteam.ui_profile.change_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.components.UiText
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.interactors.IValidateLoginUseCase
import com.clownteam.core.interactors.ValidateLoginResult
import com.clownteam.profile_interactors.GetProfileUseCaseResult
import com.clownteam.profile_interactors.IGetProfileUseCase
import com.clownteam.ui_profile.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeProfileViewModel @Inject constructor(
    private val validateLogin: IValidateLoginUseCase,
    private val getProfile: IGetProfileUseCase
) : ViewModel(), EventHandler<ChangeProfileScreenEvent> {

    var state by mutableStateOf(ChangeProfileScreenState())

    init {
        obtainEvent(ChangeProfileScreenEvent.GetProfileData)
    }

    override fun obtainEvent(event: ChangeProfileScreenEvent) {
        when (event) {
            is ChangeProfileScreenEvent.SetUsername -> {
                state = state.copy(username = event.username)
            }

            ChangeProfileScreenEvent.GetProfileData -> {
                getProfileData()
            }

            ChangeProfileScreenEvent.ApplyChanges -> {
                tryToApplyChanges()
            }

            ChangeProfileScreenEvent.MessageShown -> {
                state = state.copy(message = null)
            }
        }
    }

    private fun tryToApplyChanges() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, usernameError = null)

            delay(500)
            val loginValidationResult = validateLogin.invoke(state.username)
            val isLoginValid = handleValidateLoginResult(loginValidationResult)

            if (isLoginValid) {
                state = state.copy(message = UiText.DynamicString("Данные успешно сохранены"))
            }

            state = state.copy(isLoading = false)
        }
    }

    private fun handleValidateLoginResult(result: ValidateLoginResult): Boolean {
        when (result) {
            ValidateLoginResult.BlankLoginError -> {
                state =
                    state.copy(usernameError = UiText.StringResource(R.string.blank_login_error))
            }

            ValidateLoginResult.ShortLoginError -> {
                state =
                    state.copy(
                        usernameError = UiText.PluralsStringResource(
                            R.plurals.login_length_error,
                            IValidateLoginUseCase.MIN_LOGIN_LENGTH,
                            IValidateLoginUseCase.MIN_LOGIN_LENGTH
                        )
                    )
            }

            ValidateLoginResult.Success -> {
                state = state.copy(usernameError = null)
                return true
            }
        }

        return false
    }

    private fun getProfileData() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = getProfile.invoke()
            handleGetProfileUseCaseResult(result)
        }
    }

    private fun handleGetProfileUseCaseResult(result: GetProfileUseCaseResult) {
        when (result) {
            is GetProfileUseCaseResult.Success -> {
                state =
                    state.copy(avatarUrl = result.data.avatarUrl, username = result.data.username)
            }

            GetProfileUseCaseResult.Failed -> {
                state = state.copy(message = UiText.DynamicString("Не удалось получить данные"))
            }

            GetProfileUseCaseResult.NetworkError -> {
                state = state.copy(message = UiText.DynamicString("Отсутствует подключение к интернету"))
            }

            GetProfileUseCaseResult.Unauthorized -> {
                state = state.copy(isUnauthorized = true)
            }
        }

        state = state.copy(isLoading = false)
    }
}
