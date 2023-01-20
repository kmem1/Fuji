package com.clownteam.ui_collectiondetailed.ui

sealed class CollectionDetailedEvent {

    class GetCollection(val showLoading: Boolean = true) : CollectionDetailedEvent()

    class RateCollection(val mark: Int) : CollectionDetailedEvent()
}