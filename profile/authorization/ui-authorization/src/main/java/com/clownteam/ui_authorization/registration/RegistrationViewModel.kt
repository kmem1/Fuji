package com.clownteam.ui_authorization.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.authorization_domain.registration.RegistrationData
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
class RegistrationViewModel @Inject constructor(
    private val validateLogin: IValidateLoginUseCase,
    private val validateEmail: IValidateEmailUseCase,
    private val validatePassword: IValidatePasswordUseCase,
    private val validateRepeatedPassword: IValidateRepeatedPasswordUseCase,
    private val registerUserCase: IRegisterUseCase
) : ViewModel(), EventHandler<RegistrationEvent> {

    var state by mutableStateOf(RegistrationState())

    private val registrationResultChannel = Channel<RegistrationResult>()
    val registrationResults = registrationResultChannel.receiveAsFlow()

    override fun obtainEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is RegistrationEvent.LoginChanged -> {
                state = state.copy(login = event.login)
            }
            is RegistrationEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is RegistrationEvent.RepeatedPasswordChanged -> {
                state = state.copy(repeatedPassword = event.repeatedPassword)
            }
            RegistrationEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        viewModelScope.launch {
            val isEmailValid = handleEmailValidationResult(state.email)
            val isPasswordValid = handlePasswordValidationResult(state.password)
            val isLoginValid = handleLoginValidationResult(state.login)
            val isRepeatedPasswordValid =
                handleRepeatedPasswordValidationResult(state.password, state.repeatedPassword)

            if (isEmailValid && isPasswordValid && isLoginValid && isRepeatedPasswordValid) {
                tryToRegister()
            }
        }
    }

    private suspend fun tryToRegister() {
        val data = RegistrationData(state.login, state.email, state.password)

        when(registerUserCase.invoke(data)) {
            RegistrationUseCaseResult.Failed -> {
                registrationResultChannel.send(RegistrationResult.Failed)
            }

            RegistrationUseCaseResult.Success -> {
                registrationResultChannel.send(RegistrationResult.Success)
            }
        }
    }

    private suspend fun handleEmailValidationResult(email: String): Boolean {
        when (validateEmail.invoke(email)) {
            is ValidateEmailResult.BlankEmailError -> {
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

    private suspend fun handleLoginValidationResult(login: String): Boolean {
        when (validateLogin.invoke(login)) {
            ValidateLoginResult.BlankLoginError -> {
                state = state.copy(
                    loginError = UiText.StringResource(
                        R.string.blank_login_error
                    )
                )
            }

            ValidateLoginResult.ShortLoginError -> {
                state =
                    state.copy(
                        loginError = UiText.StringResource(
                            R.string.login_length_error,
                            IValidateLoginUseCase.MIN_LOGIN_LENGTH
                        )
                    )
            }

            ValidateLoginResult.Success -> {
                state = state.copy(loginError = null)
                return true
            }
        }

        return false
    }

    private suspend fun handleRepeatedPasswordValidationResult(
        password: String,
        repeatedPassword: String
    ): Boolean {
        val params = ValidateRepeatedPasswordParams(password, repeatedPassword)
        when (validateRepeatedPassword.invoke(params)) {
            ValidateRepeatedPasswordResult.BlankError -> {
                state = state.copy(
                    repeatedPasswordError = UiText.StringResource(
                        R.string.blank_repeated_password_error
                    )
                )
            }

            ValidateRepeatedPasswordResult.NotMatchesError -> {
                state = state.copy(
                    repeatedPasswordError = UiText.StringResource(
                        R.string.passwords_dont_match
                    )
                )
            }

            ValidateRepeatedPasswordResult.Success -> {
                state = state.copy(repeatedPasswordError = null)
                return true
            }
        }

        return false
    }

    sealed class RegistrationResult {
        object Success : RegistrationResult()

        object Failed : RegistrationResult()
    }
}