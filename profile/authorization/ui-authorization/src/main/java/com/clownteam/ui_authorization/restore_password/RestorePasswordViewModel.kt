package com.clownteam.ui_authorization.restore_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.authorization_interactors.IRestorePasswordUseCase
import com.clownteam.authorization_interactors.IValidateEmailUseCase
import com.clownteam.authorization_interactors.RestorePasswordUseCaseResult
import com.clownteam.authorization_interactors.ValidateEmailResult
import com.clownteam.components.UiText
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_authorization.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestorePasswordViewModel @Inject constructor(
    private val validateEmail: IValidateEmailUseCase,
    private val restorePassword: IRestorePasswordUseCase
) : ViewModel(), EventHandler<RestorePasswordEvent> {

    private val emailFlow = MutableStateFlow("")

    var state by mutableStateOf(RestorePasswordState(email = emailFlow))
    override fun obtainEvent(event: RestorePasswordEvent) {
        when (event) {
            is RestorePasswordEvent.EmailChanged -> {
                emailFlow.value = event.email
            }

            RestorePasswordEvent.Submit -> {
                submitEmail()
            }

            RestorePasswordEvent.FailedMessageShown -> {
                state = state.copy(failedMessage = null)
            }

            RestorePasswordEvent.NetworkErrorMessageShown -> {
                state = state.copy(networkErrorMessage = null)
            }
        }
    }

    private fun submitEmail() {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            if (isEmailValid()) {
                tryToRestorePassword()
            }
        }
    }

    private suspend fun tryToRestorePassword() {
        state = when (restorePassword.invoke(emailFlow.value)) {
            RestorePasswordUseCaseResult.Failed -> {
                state.copy(failedMessage = "Error")
            }

            RestorePasswordUseCaseResult.NetworkError -> {
                state.copy(networkErrorMessage = "Network error")
            }

            RestorePasswordUseCaseResult.Success -> {
                state.copy(isSuccess = true)
            }
        }

        state = state.copy(isLoading = false)
    }

    private suspend fun isEmailValid(): Boolean {
        when (validateEmail.invoke(emailFlow.value)) {
            ValidateEmailResult.BlankEmailError -> {
                state =
                    state.copy(emailError = UiText.StringResource(R.string.blank_email_error_string))
            }

            ValidateEmailResult.InvalidEmailError -> {
                state = state.copy(emailError = UiText.StringResource(R.string.invalid_email_error))
            }

            ValidateEmailResult.Success -> {
                state = state.copy(emailError = null)
                return true
            }
        }

        return false
    }
}