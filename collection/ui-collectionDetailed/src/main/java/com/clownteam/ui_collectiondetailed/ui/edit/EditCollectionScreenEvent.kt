package com.clownteam.ui_collectiondetailed.ui.edit

import android.graphics.Bitmap
import java.io.File

sealed class EditCollectionScreenEvent {

    class SetTitle(val title: String) : EditCollectionScreenEvent()
    class SetDescription(val description: String) : EditCollectionScreenEvent()
    class SetImage(val file: File, val bitmap: Bitmap) : EditCollectionScreenEvent()
    object GetCollection : EditCollectionScreenEvent()
    object ApplyChanges : EditCollectionScreenEvent()
    object MessageShown : EditCollectionScreenEvent()
}