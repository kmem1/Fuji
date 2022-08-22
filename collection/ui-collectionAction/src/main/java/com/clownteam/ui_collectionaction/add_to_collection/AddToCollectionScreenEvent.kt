package com.clownteam.ui_collectionaction.add_to_collection

import com.clownteam.collection_domain.CourseCollection

sealed class AddToCollectionScreenEvent {

    class AddToCollection(val collection: CourseCollection) : AddToCollectionScreenEvent()
    class SetSearchQuery(val query: String) : AddToCollectionScreenEvent()
    object ErrorMessageShown: AddToCollectionScreenEvent()
}