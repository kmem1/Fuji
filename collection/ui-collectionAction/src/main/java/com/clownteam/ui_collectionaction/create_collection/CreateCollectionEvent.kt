package com.clownteam.ui_collectionaction.create_collection

sealed class CreateCollectionEvent {

    object CreateCollection : CreateCollectionEvent()

    class TitleChanged(val newTitle: String) : CreateCollectionEvent()

    object ErrorMessageShown : CreateCollectionEvent()
}