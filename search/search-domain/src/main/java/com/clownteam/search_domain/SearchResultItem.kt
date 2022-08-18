package com.clownteam.search_domain

sealed class SearchResultItem {
    data class Course(
        val courseId: String,
        val title: String,
        val imgUrl: String,
        val rating: Float,
        val membersAmount: Int,
        val price: Float,
        val authorName: String
    ): SearchResultItem()

    data class Collection(
        val collectionId: String,
        val title: String,
        val membersAmount: Int,
        val authorName: String,
        val imgUrl: String
    ): SearchResultItem()

    data class Author(
        val authorId: String,
        val name: String,
        val membersAmount: Int,
        val imgUrl: String
    ): SearchResultItem()
}