package com.clownteam.ui_authorization.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.authorization_domain.login.LoginData
import com.clownteam.authorization_interactors.*
import com.clownteam.components.UiText
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_authorization.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmail: IValidateEmailUseCase,
    private val validatePassword: IValidatePasswordUseCase,
    private val loginUseCase: ILoginUseCase
) : ViewModel(), EventHandler<LoginEvent> {

    var state by mutableStateOf(LoginState())

    private val eventChannel = Channel<LoginViewModelEvent>()
    val events = eventChannel.receiveAsFlow()

    override fun obtainEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                state = state.copy(username = event.email)
            }

            is LoginEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }

            is LoginEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        state = state.copy(isLoading = true, isNetworkError = false)

        viewModelScope.launch {
//            val isEmailValid = handleEmailValidationResult(state.username)
            // TODO Включить обратно когда будет авторизация по email
            val isEmailValid = true
            val isPasswordValid = handlePasswordValidationResult(state.password)

            if (isEmailValid && isPasswordValid) {
                tryToLogin()
            }
        }

        state = state.copy(isLoading = false)
    }

    private suspend fun handleEmailValidationResult(email: String): Boolean {
        when (validateEmail.invoke(email)) {
            is ValidateEmailResult.BlankEmailError -> {
                state =
                    state.copy(usernameError = UiText.StringResource(R.string.blank_email_error_string))
            }

            ValidateEmailResult.InvalidEmailError -> {
                state =
                    state.copy(usernameError = UiText.StringResource(R.string.invalid_email_error))
            }

            ValidateEmailResult.Success -> {
                state = state.copy(usernameError = null)
                return true
            }
        }

        return false
    }

    private suspend fun handlePasswordValidationResult(password: String): Boolean {
        when (validatePassword.invoke(password)) {
            ValidatePasswordResult.NotEnoughLengthError -> {
                state =
                    state.copy(
                        passwordError = UiText.StringResource(
                            R.string.password_length_error,
                            IValidatePasswordUseCase.MIN_PASSWORD_LENGTH
                        )
                    )
            }

            ValidatePasswordResult.ShouldContainLettersAndDigitsError -> {
                state = state.copy(
                    passwordError = UiText.StringResource(
                        R.string.password_should_contain_letters_and_digits_error
                    )
                )
            }

            ValidatePasswordResult.Success -> {
                state = state.copy(passwordError = null)
                return true
            }
        }

        return false
    }

    private suspend fun tryToLogin() {
        val data = LoginData(state.username, state.password)

        when (val result = loginUseCase.invoke(data)) {
            is LoginUseCaseResult.Failed -> {
                eventChannel.send(LoginViewModelEvent.Failed)
            }

            LoginUseCaseResult.NetworkError -> {
                state = state.copy(isNetworkError = true)
            }

            is LoginUseCaseResult.Success -> {
                eventChannel.send(
                    LoginViewModelEvent.Success(
                        result.accessToken,
                        result.refreshToken,
                        state.username
                    )
                )
            }
        }
    }

    sealed class LoginViewModelEvent {
        class Success(val access: String, val refresh: String, val username: String) :
            LoginViewModelEvent()

        object Failed : LoginViewModelEvent()
    }
}