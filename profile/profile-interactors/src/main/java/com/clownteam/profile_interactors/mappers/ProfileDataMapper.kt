package com.clownteam.profile_interactors.mappers

import com.clownteam.core.utils.ImageUrlMapper
import com.clownteam.core.utils.ImageUrlMapperImpl
import com.clownteam.profile_datasource.network.models.get_profile.ProfileResponse
import com.clownteam.profile_domain.ActivityDayType
import com.clownteam.profile_domain.ProfileActivityDay
import com.clownteam.profile_domain.ProfileData
import kotlin.random.Random

object ProfileDataMapper : ImageUrlMapper by ImageUrlMapperImpl() {

    fun map(profileItem: ProfileResponse, baseUrl: String): ProfileData {
        return ProfileData(
            username = profileItem.username ?: "",
            avatarUrl = mapImageUrl(baseUrl, profileItem.avatarUrl ?: ""),
            backgroundImageUrl = "https://natureconservancy-h.assetsadobe.com/is/image/content/dam/tnc/nature/en/photos/Zugpsitze_mountain.jpg?crop=0%2C176%2C3008%2C1654&wid=4000&hei=2200&scl=0.752",
            lastActivityList = createRandomActivityList()
        )
    }

    private fun createRandomActivityList(): List<ProfileActivityDay> {
        val activityList = mutableListOf<ProfileActivityDay>()

        for (i in 0 until 60) {
            val month = if (i < 30) 8 else 9
            val day = (i % 30) + 1
            val dateString = "$day.0$month.22"
            val tasksCount = if (Random.nextInt(0, 3) == 0) 0 else Random.nextInt(1, 30)
            activityList.add(
                ProfileActivityDay(
                    dateString = dateString,
                    completedTasksCount = tasksCount,
                    type = ActivityDayType.getTypeByCount(tasksCount)
                )
            )
        }

        activityList.forEach { println(it) }

        return activityList
    }
}
