package com.clownteam.collection_interactors.mappers

import com.clownteam.collection_datasource.models.get_collection.CollectionCourseResponseItem
import com.clownteam.collection_datasource.models.get_collection.GetCollectionResponseItem
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.course_domain.Course

object GetCollectionResponseItemMapper {
    fun map(input: GetCollectionResponseItem, baseUrl: String): CourseCollection {
        var imgUrl =
            if (baseUrl.last() == '/') baseUrl.substring(0 until baseUrl.lastIndex) else baseUrl
        imgUrl += input.imageUrl

        return CourseCollection(
            membersAmount = input.addedNumber ?: 0,
            author = input.author ?: "",
            imageUrl = imgUrl,
            isAdded = input.isAdded ?: false,
            path = input.path ?: "",
            rating = input.rating ?: 0.0,
            title = input.title ?: "",
            courses = input.courses?.map { mapCollectionCourseResponseItem(it, baseUrl) }
                ?: emptyList(),
        )
    }

    private fun mapCollectionCourseResponseItem(
        input: CollectionCourseResponseItem,
        baseUrl: String
    ): Course {
        var imgUrl =
            if (baseUrl.last() == '/') baseUrl.substring(0 until baseUrl.lastIndex) else baseUrl
        imgUrl += input.imageUrl

        return Course(
            id = input.path ?: "",
            title = input.title ?: "",
            authorName = input.author ?: "",
            imgUrl = imgUrl,
            durationInMinutes = input.durationInMinutes ?: 0,
            rating = input.rating?.toFloat() ?: 0F,
            membersAmount = input.membersAmount ?: 0,
            description = input.description ?: "",
            price = 0F,
            marksCount = 0,
            hasCertificate = false,
            maxProgressPoints = input.progress?.maxProgress ?: 0,
            currentPoints = input.progress?.progress ?: 0,
            courseDurationInHours = (input.durationInMinutes ?: 0) / 60
        )
    }
}