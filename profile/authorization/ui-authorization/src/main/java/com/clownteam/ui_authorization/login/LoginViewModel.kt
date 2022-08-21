package com.clownteam.ui_authorization.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.authorization_domain.login.LoginData
import com.clownteam.authorization_interactors.ILoginUseCase
import com.clownteam.authorization_interactors.IValidateEmailUseCase
import com.clownteam.authorization_interactors.LoginUseCaseResult
import com.clownteam.authorization_interactors.ValidateEmailResult
import com.clownteam.components.UiText
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.ui_authorization.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmail: IValidateEmailUseCase,
    private val loginUseCase: ILoginUseCase
) : ViewModel(), EventHandler<LoginEvent>, StateHolder<MutableState<LoginState>> {

    override val state = mutableStateOf(LoginState())

    override fun obtainEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                state.value = state.value.copy(email = event.email)
            }

            is LoginEvent.PasswordChanged -> {
                state.value = state.value.copy(password = event.password)
            }

            is LoginEvent.Submit -> {
                submitData()
            }

            LoginEvent.FailMessageShown -> {
                state.value = state.value.copy(errorMessage = null)
            }
        }
    }

    private fun submitData() {
        state.value = state.value.copy(isLoading = true, isNetworkError = false)

        viewModelScope.launch {
            val isEmailValid = handleEmailValidationResult(state.value.email)

            if (isEmailValid) {
                tryToLogin()
            }
        }

        state.value = state.value.copy(isLoading = false)
    }

    private suspend fun handleEmailValidationResult(email: String): Boolean {
        when (validateEmail.invoke(email)) {
            is ValidateEmailResult.BlankEmailError -> {
                state.value =
                    state.value.copy(emailError = UiText.StringResource(R.string.blank_email_error_string))
            }

            ValidateEmailResult.InvalidEmailError -> {
                state.value =
                    state.value.copy(emailError = UiText.StringResource(R.string.invalid_email_error))
            }

            ValidateEmailResult.Success -> {
                state.value = state.value.copy(emailError = null)
                return true
            }
        }

        return false
    }

    private suspend fun tryToLogin() {
        val data = LoginData(state.value.email, state.value.password)

        when (loginUseCase.invoke(data)) {
            is LoginUseCaseResult.Failed -> {
                state.value =
                    state.value.copy(
                        errorMessage = UiText.StringResource(R.string.login_fail_message),
                        isNetworkError = false
                    )
            }

            LoginUseCaseResult.NetworkError -> {
                state.value = state.value.copy(
                    isNetworkError = true,
                    errorMessage = UiText.StringResource(R.string.network_error)
                )
            }

            is LoginUseCaseResult.Success -> {
                state.value = state.value.copy(
                    isNetworkError = false,
                    isSuccess = true
                )
            }
        }
    }
}