package com.clownteam.course_interactors.mappers

import com.clownteam.core.utils.ImageUrlMapper
import com.clownteam.core.utils.ImageUrlMapperImpl
import com.clownteam.course_datasource.network.models.get_user_courses.GetUserCourseItem
import com.clownteam.course_domain.Course

object UserCourseResponseMapper: ImageUrlMapper by ImageUrlMapperImpl() {

    fun map(input: GetUserCourseItem, baseUrl: String): Course {
        return Course(
            id = input.path ?: "",
            title = input.title ?: "",
            authorName = input.author?.username ?: "",
            imgUrl = mapImageUrl(baseUrl, input.imageUrl ?: ""),
            durationInMinutes = input.durationInMinutes ?: 0,
            rating = input.rating?.toFloat() ?: 0F,
            membersAmount = input.membersAmount ?: 0,
            description = "",
            price = 0F,
            marksCount = 0,
            hasCertificate = false,
            maxProgressPoints = input.progress?.maxProgress ?: 0,
            currentPoints = input.progress?.progress ?: 0,
            courseDurationInHours = 0,
            isStarted = input.statusProgress != null
        )
    }
}