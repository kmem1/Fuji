package com.clownteam.ui_profile.change_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.components.UiText
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.interactors.IValidatePasswordUseCase
import com.clownteam.core.interactors.ValidatePasswordResult
import com.clownteam.ui_profile.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val validatePassword: IValidatePasswordUseCase
) : ViewModel(),
    EventHandler<ChangePasswordScreenEvent> {

    var state by mutableStateOf(ChangePasswordScreenState())

    override fun obtainEvent(event: ChangePasswordScreenEvent) {
        when (event) {
            ChangePasswordScreenEvent.ApplyChanges -> {
                tryToApplyChanges()
            }

            ChangePasswordScreenEvent.MessageShown -> {
                state = state.copy(message = null)
            }

            is ChangePasswordScreenEvent.SetCurrentPassword -> {
                state = state.copy(currentPassword = event.password)
            }

            is ChangePasswordScreenEvent.SetNewPassword -> {
                state = state.copy(newPassword = event.password)
            }
        }
    }

    private fun tryToApplyChanges() {
        viewModelScope.launch {
            val newPasswordValidationResult = validatePassword.invoke(state.newPassword)

            val isNewPasswordValid = handleValidatePasswordResult(newPasswordValidationResult)
            if (!isNewPasswordValid) return@launch


        }
    }

    private fun handleValidatePasswordResult(result: ValidatePasswordResult): Boolean {
        when (result) {
            ValidatePasswordResult.NotEnoughLengthError -> {
                state =
                    state.copy(
                        newPasswordError = UiText.StringResource(
                            R.string.password_length_error,
                            IValidatePasswordUseCase.MIN_PASSWORD_LENGTH
                        )
                    )
            }

            ValidatePasswordResult.ShouldContainLettersAndDigitsError -> {
                state = state.copy(
                    newPasswordError = UiText.StringResource(
                        R.string.password_should_contain_letters_and_digits_error
                    )
                )
            }

            ValidatePasswordResult.ShouldContainLowerAndUpperCaseError -> {
                state = state.copy(
                    newPasswordError = UiText.StringResource(
                        R.string.password_should_contain_lower_and_upper_cases
                    )
                )
            }

            ValidatePasswordResult.ShouldContainNonLetterSymbol -> {
                state = state.copy(
                    newPasswordError = UiText.StringResource(
                        R.string.password_should_contains_symbols
                    )
                )
            }

            ValidatePasswordResult.Success -> {
                state = state.copy(newPasswordError = null)
                return true
            }
        }

        return false
    }
}