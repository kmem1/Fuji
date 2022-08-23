package com.clownteam.ui_collectionaction.add_to_collection

import com.clownteam.collection_domain.CollectionSortOption
import com.clownteam.collection_domain.CollectionSortType
import com.clownteam.collection_domain.CourseCollection

sealed class AddToCollectionScreenEvent {

    class AddToCollection(val collection: CourseCollection) : AddToCollectionScreenEvent()
    class SetSearchQuery(val query: String) : AddToCollectionScreenEvent()
    class SetSortOption(val sortOption: CollectionSortOption) : AddToCollectionScreenEvent()
    object ErrorMessageShown: AddToCollectionScreenEvent()
}