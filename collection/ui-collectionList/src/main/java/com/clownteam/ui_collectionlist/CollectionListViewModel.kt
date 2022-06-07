package com.clownteam.ui_collectionlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class CollectionListViewModel : ViewModel(), EventHandler<CollectionListEvent>,
    StateHolder<MutableState<CollectionListState>> {

    override fun obtainEvent(event: CollectionListEvent) {
        when(event) {
            CollectionListEvent.GetCollections -> {

            }
        }
    }

    override val state: MutableState<CollectionListState> =
        mutableStateOf(CollectionListState.Loading)
}