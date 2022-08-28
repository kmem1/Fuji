package com.clownteam.user_archive_domain

sealed class ArchiveItem {

    data class Collection(
        val collectionId: String,
        val title: String,
        val authorName: String,
        val authorPath: String,
        val imgUrl: String
    ) : ArchiveItem()

    data class Course(
        val courseId: String,
        val title: String,
        val authorName: String,
        val authorPath: String,
        val imgUrl: String
    ) : ArchiveItem()
}