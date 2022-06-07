package com.clownteam.ui_collectionlist

import com.clownteam.collection_domain.CourseCollection

sealed class CollectionListState {

    object Loading : CollectionListState()

    class Data(val collections: List<CourseCollection>): CollectionListState()

    class Error(val message: String): CollectionListState()
}