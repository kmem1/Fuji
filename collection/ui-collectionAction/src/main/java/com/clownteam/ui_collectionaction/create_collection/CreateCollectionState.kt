package com.clownteam.ui_collectionaction.create_collection

sealed class CreateCollectionState {

    object Loading: CreateCollectionState()

    object Idle: CreateCollectionState()

}