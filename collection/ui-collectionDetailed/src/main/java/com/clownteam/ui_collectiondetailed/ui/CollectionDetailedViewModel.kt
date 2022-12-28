package com.clownteam.ui_collectiondetailed.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.collection_interactors.GetCollectionUseCaseResult
import com.clownteam.collection_interactors.IGetCollectionUseCase
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailedViewModel @Inject constructor(
    private val getCollection: IGetCollectionUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), EventHandler<CollectionDetailedEvent>,
    StateHolder<MutableState<CollectionDetailedState>> {

    override val state: MutableState<CollectionDetailedState> =
        mutableStateOf(CollectionDetailedState.Loading)

    private val collectionId = savedStateHandle.get<String>(COLLECTION_ID_ARG_KEY)

    override fun obtainEvent(event: CollectionDetailedEvent) {
        when (event) {
            is CollectionDetailedEvent.GetCollection -> {
                getCollection()
            }
        }
    }

    private fun getCollection() {
        viewModelScope.launch {
            if (collectionId == null) {
                updateState(CollectionDetailedState.Error)
                return@launch
            }

            updateState(CollectionDetailedState.Loading)

            val collectionResult = getCollection.invoke(collectionId)

            handleGetCollectionUseCaseResult(collectionResult)
        }
    }

    private fun handleGetCollectionUseCaseResult(result: GetCollectionUseCaseResult) {
        when (result) {
            GetCollectionUseCaseResult.Failed -> {
                updateState(CollectionDetailedState.Error)
            }

            GetCollectionUseCaseResult.NetworkError -> {
                updateState(CollectionDetailedState.Error)
            }

            is GetCollectionUseCaseResult.Success -> {
                updateState(CollectionDetailedState.Data(result.data))
            }

            GetCollectionUseCaseResult.Unauthorized -> {
                updateState(CollectionDetailedState.Unauthorized)
            }
        }
    }

    private fun updateState(newState: CollectionDetailedState) {
        state.value = newState
    }

    companion object {
        const val COLLECTION_ID_ARG_KEY = "collectionId"
    }
}
