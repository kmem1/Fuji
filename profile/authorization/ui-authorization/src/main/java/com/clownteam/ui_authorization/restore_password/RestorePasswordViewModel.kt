package com.clownteam.ui_authorization.restore_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.authorization_interactors.IValidateEmailUseCase
import com.clownteam.authorization_interactors.ValidateEmailResult
import com.clownteam.components.UiText
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_authorization.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestorePasswordViewModel @Inject constructor(
    private val validateEmail: IValidateEmailUseCase
) : ViewModel(), EventHandler<RestorePasswordEvent> {

    var state by mutableStateOf(RestorePasswordState())

    override fun obtainEvent(event: RestorePasswordEvent) {
        when (event) {
            is RestorePasswordEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }

            RestorePasswordEvent.Submit -> {
                submitEmail()
            }
        }
    }

    private fun submitEmail() {
        viewModelScope.launch {
            if (isEmailValid()) {
                state = state.copy(isSuccess = true)
            }
        }
    }

    private suspend fun isEmailValid(): Boolean {
        when (validateEmail.invoke(state.email)) {
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