package com.clownteam.ui_authorization.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.authorization_interactors.IValidateEmailUseCase
import com.clownteam.authorization_interactors.IValidatePasswordUseCase
import com.clownteam.authorization_interactors.ValidateEmailResult
import com.clownteam.authorization_interactors.ValidatePasswordResult
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
    private val validatePassword: IValidatePasswordUseCase
) : ViewModel(), EventHandler<LoginEvent> {

    var state by mutableStateOf(LoginState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    override fun obtainEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }

            is LoginEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }

            is LoginEvent.Submit -> {
                submitData()
            }
        }
        Log.d("Kmem", state.toString())
    }

    private fun submitData() {
        viewModelScope.launch {
            val isEmailValid = handleEmailValidationResult(state.email)
            val isPasswordValid = handlePasswordValidationResult(state.password)

            if (isEmailValid && isPasswordValid) {
                validationEventChannel.send(ValidationEvent.Success)
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
                state = state.copy(passwordError = UiText.StringResource(
                            R.string.password_should_contain_letters_and_digits_error))
            }

            ValidatePasswordResult.Success -> {
                state = state.copy(passwordError = null)
                return true
            }
        }

        return false
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}