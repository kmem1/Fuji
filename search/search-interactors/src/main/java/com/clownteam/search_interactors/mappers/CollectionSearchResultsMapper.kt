package com.clownteam.search_interactors.mappers

import com.clownteam.core.utils.ImageUrlMapper
import com.clownteam.core.utils.ImageUrlMapperImpl
import com.clownteam.search_datasource.models.get_collections.SearchCollectionResultItem
import com.clownteam.search_domain.SearchResultItem

object CollectionSearchResultsMapper : ImageUrlMapper by ImageUrlMapperImpl() {

    fun map(input: SearchCollectionResultItem, baseUrl: String): SearchResultItem.Collection {
        return SearchResultItem.Collection(
            collectionId = input.path ?: "",
            title = input.title ?: "",
            imgUrl = mapImageUrl(baseUrl, input.imageUrl ?: ""),
            membersAmount = input.addedNumber ?: 0,
            authorName = input.author?.username ?: ""
        )
    }
}