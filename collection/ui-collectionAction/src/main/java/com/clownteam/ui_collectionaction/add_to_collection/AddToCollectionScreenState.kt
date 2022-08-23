package com.clownteam.ui_collectionaction.add_to_collection

import com.clownteam.collection_domain.CollectionSortOption
import com.clownteam.collection_domain.CollectionSortType
import com.clownteam.components.UiText

data class AddToCollectionScreenState(
    val courseId: String,
    val isLoading: Boolean = false,
    val isCollectionListLoading: Boolean = false,
    val searchQuery: String = "",
    val sortOption: CollectionSortOption = CollectionSortOption(type = CollectionSortType.Rating),
    val shouldSearchItems: Boolean = false,
    val getCollectionsErrorMessage: UiText? = null,
    val addToCollectionErrorMessage: UiText? = null,
    val isUnauthorized: Boolean = false,
    val isSuccess: Boolean = false
)