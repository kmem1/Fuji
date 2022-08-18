package com.clownteam.search_interactors.mappers

import com.clownteam.search_datasource.models.get_collections.SearchCollectionResultItem
import com.clownteam.search_domain.SearchResultItem

object CollectionSearchResultsMapper {

    fun map(input: SearchCollectionResultItem, baseUrl: String): SearchResultItem.Collection {
        var imgUrl =
            if (baseUrl.last() == '/') baseUrl.substring(0 until baseUrl.lastIndex) else baseUrl
        imgUrl += input.imageUrl

        return SearchResultItem.Collection(
            collectionId = input.path ?: "",
            title = input.title ?: "",
            imgUrl = imgUrl,
            membersAmount = input.addedNumber ?: 0,
            authorName = input.author?.username ?: ""
        )
    }
}