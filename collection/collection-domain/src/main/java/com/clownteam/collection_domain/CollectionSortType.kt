package com.clownteam.collection_domain

enum class CollectionSortType {
    Rating,
    Title,
    Date
}

enum class SortDirection {
    ASC,
    DESC;

    fun reverse(): SortDirection = if (this == DESC) ASC else DESC
}

data class CollectionSortOption(val type: CollectionSortType, val direction: SortDirection = SortDirection.DESC)