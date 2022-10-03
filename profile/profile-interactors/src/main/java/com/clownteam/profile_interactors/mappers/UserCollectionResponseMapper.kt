package com.clownteam.profile_interactors.mappers

import com.clownteam.core.utils.ImageUrlMapper
import com.clownteam.core.utils.ImageUrlMapperImpl
import com.clownteam.profile_datasource.network.models.get_collections.UserCollectionResultItem
import com.clownteam.profile_domain.ProfileCollection

object UserCollectionResponseMapper : ImageUrlMapper by ImageUrlMapperImpl() {

    fun map(input: UserCollectionResultItem, baseUrl: String): ProfileCollection {
        return ProfileCollection(
            collectionId = input.path ?: "",
            title = input.title ?: "",
            authorName = input.author?.username ?: "",
            imageUrl = mapImageUrl(baseUrl, input.imageUrl ?: "")
        )
    }
}