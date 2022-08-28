package com.clownteam.user_archive_domain

sealed class ArchiveCategory {

    object Courses: ArchiveCategory()
    object Collections: ArchiveCategory()
}