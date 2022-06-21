package com.clownteam.collection_interactors.mappers

import com.clownteam.collection_datasource.models.get_user_collections.GetUserCollectionsResponseItem
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.collection_domain.CourseCollectionAuthor

object GetUserCollectionsResponseItemMapper {

    fun map(input: GetUserCollectionsResponseItem, baseUrl: String): CourseCollection {
        var imgUrl =
            if (baseUrl.last() == '/') baseUrl.substring(0 until baseUrl.lastIndex) else baseUrl
        imgUrl += input.imageUrl

        return CourseCollection(
            membersAmount = 0,
            author = CourseCollectionAuthor(
                input.author?.id ?: "",
                name = input.author?.username ?: "",
                avatar_url = input.author?.avatarUrl ?: ""
            ),
            courses = emptyList(),
            imageUrl = imgUrl,
            isAdded = input.isAdded ?: false,
            id = input.path ?: "",
            rating = 0.0,
            title = input.title ?: ""
        )
    }
}