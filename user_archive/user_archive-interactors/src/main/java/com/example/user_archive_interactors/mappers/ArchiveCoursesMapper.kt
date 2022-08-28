package com.example.user_archive_interactors.mappers

import com.clownteam.core.utils.ImageUrlMapper
import com.clownteam.core.utils.ImageUrlMapperImpl
import com.clownteam.user_archive_datasource.models.get_courses.ArchiveCourseResultItem
import com.clownteam.user_archive_domain.ArchiveItem

object ArchiveCoursesMapper : ImageUrlMapper by ImageUrlMapperImpl() {

    fun map(input: ArchiveCourseResultItem, baseUrl: String): ArchiveItem.Course =
        ArchiveItem.Course(
            courseId = input.path ?: "",
            title = input.title ?: "",
            authorName = input.author?.username ?: "",
            authorPath = input.author?.id ?: "",
            imgUrl = mapImageUrl(baseUrl, input.imageUrl ?: "")
        )
}