package com.clownteam.ui_collectionaction.create_collection

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateCollectionViewModel @Inject constructor(

) : EventHandler<CreateCollectionEvent>, StateHolder<MutableState<CreateCollectionState>> {

    override val state: MutableState<CreateCollectionState> = mutableStateOf(CreateCollectionState.Idle)

    override fun obtainEvent(event: CreateCollectionEvent) {
        when(event) {
            is CreateCollectionEvent.CreateCollection -> {

            }
        }
    }
}