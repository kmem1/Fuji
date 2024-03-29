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
import com.clownteam.core.interactors.IValidateLoginUseCase
import com.clownteam.core.interactors.IValidatePasswordUseCase
import com.clownteam.core.interactors.ValidateLoginResult
import com.clownteam.core.interactors.ValidatePasswordResult
import com.clownteam.ui_authorization.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val validateLogin: IValidateLoginUseCase,
    private val validateEmail: IValidateEmailUseCase,
    private val validatePassword: IValidatePasswordUseCase,
    private val validateRepeatedPassword: IValidateRepeatedPasswordUseCase,
    private val registerUserCase: IRegistrationUseCase
) : ViewModel(), EventHandler<RegistrationEvent> {

    private val emailFlow = MutableStateFlow("")
    private val loginFlow = MutableStateFlow("")
    private val passwordFlow = MutableStateFlow("")
    private val repeatedPasswordFlow = MutableStateFlow("")

    var state by mutableStateOf(
        RegistrationState(
            email = emailFlow,
            login = loginFlow,
            password = passwordFlow,
            repeatedPassword = repeatedPasswordFlow
        )
    )

    override fun obtainEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.EmailChanged -> {
                emailFlow.value = event.email
            }
            is RegistrationEvent.LoginChanged -> {
                loginFlow.value = event.login
            }
            is RegistrationEvent.PasswordChanged -> {
                passwordFlow.value = event.password
            }
            is RegistrationEvent.RepeatedPasswordChanged -> {
                repeatedPasswordFlow.value = event.repeatedPassword
            }
            RegistrationEvent.Submit -> {
                submitData()
            }
            RegistrationEvent.MessageShown -> {
                state = state.copy(message = null)
            }
        }
    }

    private fun submitData() {
        viewModelScope.launch {
            val isEmailValid = handleEmailValidationResult(emailFlow.value)
            val isPasswordValid = handlePasswordValidationResult(passwordFlow.value)
            val isLoginValid = handleLoginValidationResult(loginFlow.value)
            val isRepeatedPasswordValid = handleRepeatedPasswordValidationResult(
                passwordFlow.value,
                repeatedPasswordFlow.value
            )

            if (isEmailValid && isPasswordValid && isLoginValid && isRepeatedPasswordValid) {
                tryToRegister()
            }
        }
    }

    private suspend fun tryToRegister() {
        state = state.copy(isLoading = true)

        val data = RegistrationData(loginFlow.value, emailFlow.value, passwordFlow.value)

        state = when (registerUserCase.invoke(data)) {
            is RegistrationUseCaseResult.Failed -> {
                state.copy(message = UiText.StringResource(R.string.registration_fail_message))
            }

            RegistrationUseCaseResult.Success -> {
                state.copy(
                    isSuccessRegistration = true,
                    message = UiText.StringResource(R.string.registration_success_message)
                )
            }

            RegistrationUseCaseResult.NetworkError -> {
                state.copy(message = UiText.StringResource(R.string.network_error))
            }
        }

        state = state.copy(isLoading = false)
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

            ValidatePasswordResult.ShouldContainLowerAndUpperCaseError -> {
                state = state.copy(
                    passwordError = UiText.StringResource(
                        R.string.password_should_contain_lower_and_upper_cases
                    )
                )
            }

            ValidatePasswordResult.ShouldContainNonLetterSymbol -> {
                state = state.copy(
                    passwordError = UiText.StringResource(
                        R.string.password_should_contains_symbols
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
                        loginError = UiText.PluralsStringResource(
                            R.plurals.login_length_error,
                            IValidateLoginUseCase.MIN_LOGIN_LENGTH,
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
                        R.string.passwords_do_not_match
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
}