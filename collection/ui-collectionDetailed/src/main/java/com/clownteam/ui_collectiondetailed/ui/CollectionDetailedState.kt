package com.clownteam.ui_collectiondetailed.ui

import com.clownteam.collection_domain.CourseCollection

sealed class CollectionDetailedState {

    object Loading : CollectionDetailedState()

    data class Data(val collection: CourseCollection) : CollectionDetailedState()

    object Error : CollectionDetailedState()

    object Unauthorized : CollectionDetailedState()
}