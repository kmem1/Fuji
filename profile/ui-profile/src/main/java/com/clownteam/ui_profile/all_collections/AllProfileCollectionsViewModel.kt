package com.clownteam.ui_profile.all_collections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.components.UiText
import com.clownteam.core.domain.EventHandler
import com.clownteam.profile_interactors.GetProfileCollectionsUseCaseParams
import com.clownteam.profile_interactors.GetProfileCollectionsUseCaseResult
import com.clownteam.profile_interactors.IGetProfileCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllProfileCollectionsViewModel @Inject constructor(
    private val getProfileCollectionsUseCase: IGetProfileCollectionsUseCase
) : ViewModel(), EventHandler<AllProfileCollectionsScreenEvent> {

    var state by mutableStateOf(AllProfileCollectionsScreenState())

    init {
        obtainEvent(AllProfileCollectionsScreenEvent.GetCollections)
    }

    override fun obtainEvent(event: AllProfileCollectionsScreenEvent) {
        when (event) {
            AllProfileCollectionsScreenEvent.GetCollections -> {
                getCollections()
            }
        }
    }

    private fun getCollections() {
        state = state.copy(isLoading = true)

        viewModelScope.launch {
            val result = getProfileCollectionsUseCase.invoke(GetProfileCollectionsUseCaseParams())

            handleGetProfileCollectionsUseCaseResult(result)
        }
    }

    private fun handleGetProfileCollectionsUseCaseResult(result: GetProfileCollectionsUseCaseResult) {
        when (result) {
            GetProfileCollectionsUseCaseResult.Failed -> {
                state = state.copy(errorMessage = UiText.DynamicString("Server Error"))
            }

            GetProfileCollectionsUseCaseResult.NetworkError -> {
                state = state.copy(errorMessage = UiText.DynamicString("Network Error"))
            }

            is GetProfileCollectionsUseCaseResult.Success -> {
                state = state.copy(collections = result.data)
            }

            GetProfileCollectionsUseCaseResult.Unauthorized -> {
                state = state.copy(isUnauthorized = true)
            }
        }

        state = state.copy(isLoading = false)
    }
}
