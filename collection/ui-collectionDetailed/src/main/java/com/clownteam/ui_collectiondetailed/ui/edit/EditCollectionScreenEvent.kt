package com.clownteam.ui_collectiondetailed.ui.edit

sealed class EditCollectionScreenEvent {

    class SetTitle(val title: String) : EditCollectionScreenEvent()
    class SetDescription(val description: String) : EditCollectionScreenEvent()
    object GetCollection : EditCollectionScreenEvent()
    object ApplyChanges : EditCollectionScreenEvent()
    object MessageShown : EditCollectionScreenEvent()
}