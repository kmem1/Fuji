package com.clownteam.profile_interactors.mappers

import com.clownteam.core.utils.ImageUrlMapper
import com.clownteam.core.utils.ImageUrlMapperImpl
import com.clownteam.profile_datasource.network.models.get_user_courses.GetUserCourseItem
import com.clownteam.profile_domain.ProfileCourse

object UserCourseResponseMapper : ImageUrlMapper by ImageUrlMapperImpl() {

    fun map(input: GetUserCourseItem, baseUrl: String): ProfileCourse {
        return ProfileCourse(
            courseId = input.path ?: "",
            title = input.title ?: "",
            authorName = input.author?.username ?: "",
            imageUrl = mapImageUrl(baseUrl, input.imageUrl ?: ""),
            rating = input.rating?.toFloat() ?: 0F,
            price = input.price?.toInt() ?: 0,
            marksAmount = 0,
        )
    }
}