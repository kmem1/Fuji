package com.clownteam.course_interactors.mappers

import com.clownteam.course_datasource.network.models.get_course_info.CourseInfoResponse
import com.clownteam.course_domain.Course
import com.clownteam.course_domain.CourseInfoUI
import com.clownteam.course_domain.ForWhomCourseDescriptionItemUI

object CourseInfoResponseMapper {
    fun map(input: CourseInfoResponse, baseUrl: String): MapperResult {


        val forWhomItems = input.info?.fits?.map {
            ForWhomCourseDescriptionItemUI(
                title = it.title ?: "",
                description = it.description ?: ""
            )
        } ?: emptyList()

        val totalStarsNumber = input.info?.stars?.totalNumber?.coerceAtLeast(1) ?: 1

        val starsPercentage = mapOf(
            Pair(5, (input.info?.stars?.five ?: 0) / totalStarsNumber * 100),
            Pair(4, (input.info?.stars?.four ?: 0) / totalStarsNumber * 100),
            Pair(3, (input.info?.stars?.three ?: 0) / totalStarsNumber * 100),
            Pair(2, (input.info?.stars?.two ?: 0) / totalStarsNumber * 100) ,
            Pair(1, (input.info?.stars?.one ?: 0) / totalStarsNumber * 100)
        )

        val courseInfo = CourseInfoUI(
            goalDescription = input.info?.mainInfo?.goalDescription ?: "",
            forWhomCourseDescriptionItems = forWhomItems,
            learningSkillsDescriptionItems = input.info?.skills ?: emptyList(),
            moduleItems = emptyList(),
            starsPercentage = starsPercentage,
            reviewItems = emptyList()
        )

        var imgUrl = if (baseUrl.last() == '/') baseUrl.substring(0 until baseUrl.lastIndex) else baseUrl
        imgUrl += input.mainInfo?.imageUrl

        val course = Course(
            id = "",
            title = input.mainInfo?.title ?: "",
            authorName = input.mainInfo?.author ?: "",
            imgUrl = imgUrl,
            durationInMinutes = input.mainInfo?.durationInMinutes ?: 0,
            rating = input.mainInfo?.rating?.toFloat() ?: 0F,
            membersAmount = input.mainInfo?.membersAmount ?: 0,
            description = "",
            price = input.mainInfo?.price?.toFloat() ?: 0F,
            marksCount = input.info?.stars?.totalNumber ?: 0,
            hasCertificate = false,
            maxProgressPoints = 0,
            currentPoints = 0,
            courseDurationInHours = 0
        )

        return MapperResult(course = course, courseInfoUI = courseInfo)
    }

    data class MapperResult(val course: Course, val courseInfoUI: CourseInfoUI)
}
