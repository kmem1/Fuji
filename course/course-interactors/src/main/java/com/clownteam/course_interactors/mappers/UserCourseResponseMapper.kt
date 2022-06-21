package com.clownteam.course_interactors.mappers

import com.clownteam.course_datasource.network.models.get_user_courses.GetUserCourseItem
import com.clownteam.course_domain.Course

object UserCourseResponseMapper {

    fun map(input: GetUserCourseItem, baseUrl: String): Course {
        var imgUrl = if (baseUrl.last() == '/') baseUrl.substring(0 until baseUrl.lastIndex) else baseUrl
        imgUrl += input.imageUrl

        return Course(
            id = "",
            title = input.title ?: "",
            authorName = input.author?.username ?: "",
            imgUrl = imgUrl,
            durationInMinutes = input.durationInMinutes ?: 0,
            rating = input.rating?.toFloat() ?: 0F,
            membersAmount = input.membersAmount ?: 0,
            description = "",
            price = 0F,
            marksCount = 0,
            hasCertificate = false,
            maxProgressPoints = 0,
            currentPoints = 0,
            courseDurationInHours = 0
        )
    }
}