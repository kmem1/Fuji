package com.clownteam.search_interactors.mappers

import com.clownteam.search_datasource.models.get_courses.SearchCourseResultItem
import com.clownteam.search_domain.SearchResultItem

object CourseSearchResultsMapper {

    fun map(input: SearchCourseResultItem, baseUrl: String): SearchResultItem.Course {
        var imgUrl =
            if (baseUrl.last() == '/') baseUrl.substring(0 until baseUrl.lastIndex) else baseUrl
        imgUrl += input.imageUrl

        return SearchResultItem.Course(
            courseId = input.path ?: "",
            title = input.title ?: "",
            imgUrl = imgUrl,
            rating = input.rating?.toFloat() ?: 0F,
            membersAmount = input.membersAmount ?: 0,
            price = input.price?.toFloat() ?: 0F,
            authorName = input.author?.username ?: ""
        )
    }
}