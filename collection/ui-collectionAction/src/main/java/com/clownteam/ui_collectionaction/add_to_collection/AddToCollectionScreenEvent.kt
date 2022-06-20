package com.clownteam.ui_collectionaction.add_to_collection

import com.clownteam.collection_domain.CourseCollection

sealed class AddToCollectionScreenEvent {

    object GetMyCollections : AddToCollectionScreenEvent()

    class AddToCollection(val collection: CourseCollection) : AddToCollectionScreenEvent()

}