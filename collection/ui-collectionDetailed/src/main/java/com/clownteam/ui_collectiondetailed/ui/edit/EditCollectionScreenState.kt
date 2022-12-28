package com.clownteam.ui_collectiondetailed.ui.edit

import com.clownteam.components.UiText

data class EditCollectionScreenState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isUnauthorized: Boolean = false,
    val isError: Boolean = false,
    val collectionImgUrl: String = "",
    val title: String = "",
    val titleError: UiText? = null,
    val description: String = "",
    val descriptionError: UiText? = null,
    val message: UiText? = null
)
