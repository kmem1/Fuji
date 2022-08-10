package com.clownteam.ui_collectionlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.collection_interactors.GetMyCollectionsUseCaseResult
import com.clownteam.collection_interactors.IGetMyCollectionsUseCase
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionListViewModel @Inject constructor(
    private val getCollections: IGetMyCollectionsUseCase
) : ViewModel(), EventHandler<CollectionListEvent>,
    StateHolder<MutableState<CollectionListState>> {

    override val state: MutableState<CollectionListState> =
        mutableStateOf(CollectionListState.Loading)

    init {
        obtainEvent(CollectionListEvent.GetCollections)
    }

    override fun obtainEvent(event: CollectionListEvent) {
        when (event) {
            CollectionListEvent.GetCollections -> {
                getCollections()
            }
        }
    }

    private fun getCollections() {
        viewModelScope.launch {
            state.value = CollectionListState.Loading

            val result = getCollections.invoke()

            handleGetCollectionsResult(result)
        }
    }

    private fun handleGetCollectionsResult(result: GetMyCollectionsUseCaseResult) {
        when (result) {
            GetMyCollectionsUseCaseResult.Failed -> {
                state.value = CollectionListState.Error(message = "Неизвестная ошибка")
            }

            GetMyCollectionsUseCaseResult.NetworkError -> {
                state.value = CollectionListState.Error(message = "Нет подключения к интернету")
            }

            is GetMyCollectionsUseCaseResult.Success -> {
                state.value = CollectionListState.Data(result.data)
            }

            GetMyCollectionsUseCaseResult.Unauthorized -> {
                state.value = CollectionListState.Error(message = "Необходима авторизация")
            }
        }
    }
}
