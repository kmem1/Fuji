package com.clownteam.ui_collectionaction.create_collection

sealed class CreateCollectionEvent {

    class CreateCollection(val title: String) : CreateCollectionEvent()
}