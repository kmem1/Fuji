package com.clownteam.ui_profile.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.core.domain.EventHandler
import com.clownteam.profile_interactors.ISignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signOutUseCase: ISignOutUseCase
) : ViewModel(), EventHandler<SettingsScreenEvent> {

    var state by mutableStateOf(SettingsScreenState())

    override fun obtainEvent(event: SettingsScreenEvent) {
        when (event) {
            SettingsScreenEvent.SignOut -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            signOutUseCase.invoke()
            state = state.copy(signedOut = true)
        }
    }
}