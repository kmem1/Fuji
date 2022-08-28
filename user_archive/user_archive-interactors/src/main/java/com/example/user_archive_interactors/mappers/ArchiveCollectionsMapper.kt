package com.example.user_archive_interactors.mappers

import com.clownteam.core.utils.ImageUrlMapper
import com.clownteam.core.utils.ImageUrlMapperImpl
import com.clownteam.user_archive_datasource.models.get_collections.ArchiveCollectionResultItem
import com.clownteam.user_archive_domain.ArchiveItem

object ArchiveCollectionsMapper : ImageUrlMapper by ImageUrlMapperImpl() {

    fun map(input: ArchiveCollectionResultItem, baseUrl: String): ArchiveItem.Collection =
        ArchiveItem.Collection(
            collectionId = input.path ?: "",
            title = input.title ?: "",
            authorName = input.author?.username ?: "",
            authorPath = input.author?.path ?: "",
            imgUrl = mapImageUrl(baseUrl, input.imageUrl ?: "")
        )
}