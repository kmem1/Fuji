package com.clownteam.course_interactors.mappers

import com.clownteam.course_datasource.network.models.get_courses.CourseInfoRemoteModel
import com.clownteam.course_domain.Course

internal object CourseInfoRemoteModelMapper {
    fun map(courseInfo: CourseInfoRemoteModel, baseUrl: String): Course {
        var imgUrl = if (baseUrl.last() == '/') baseUrl.substring(0 until baseUrl.lastIndex) else baseUrl
        imgUrl += courseInfo.imageUrl
        println(imgUrl)
        return Course(
            id = courseInfo.path ?: "",
            title = courseInfo.title ?: "",
            imgUrl = imgUrl,
            description = courseInfo.description ?: "",
            price = 0F,
            durationInMinutes = courseInfo.durationInMinutes ?: 0,
            rating = courseInfo.rating?.toFloat() ?: 0F,
            marksCount = 0,
            membersAmount = courseInfo.membersAmount ?: 0,
            hasCertificate = false,
            maxProgressPoints = courseInfo.progress?.maxProgress ?: 0,
            currentPoints = courseInfo.progress?.progress ?: 0,
            authorName = courseInfo.author?.username ?: "",
            courseDurationInHours = (courseInfo.durationInMinutes ?: 0) / 60,
            isStarted = courseInfo.statusProgress != null
        )
    }
}