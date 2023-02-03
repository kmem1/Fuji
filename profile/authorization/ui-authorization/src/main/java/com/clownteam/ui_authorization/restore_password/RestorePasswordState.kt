package com.clownteam.ui_authorization.restore_password

import com.clownteam.components.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class RestorePasswordState(
    val email: Flow<String> = emptyFlow(),
    val emailError: UiText? = null,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    var networkErrorMessage: String? = null,
    var failedMessage: String? = null
)
