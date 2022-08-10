package com.clownteam.ui_collectionaction.create_collection

import com.clownteam.components.UiText

data class CreateCollectionState(
    val isLoading: Boolean = false,
    val collectionTitle: String = "",
    val errorMessage: UiText? = null,
    val isUnauthorized: Boolean = false,
    val isSuccess: Boolean = false
)
