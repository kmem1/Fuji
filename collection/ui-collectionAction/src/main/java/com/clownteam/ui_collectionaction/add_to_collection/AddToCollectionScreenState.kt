package com.clownteam.ui_collectionaction.add_to_collection

import com.clownteam.collection_domain.CourseCollection

sealed class AddToCollectionScreenState {
    object Loading : AddToCollectionScreenState()

    class Data(val collections: List<CourseCollection>, val courseId: String) :
        AddToCollectionScreenState()

    object Error : AddToCollectionScreenState()

    object Unauthorized : AddToCollectionScreenState()
}