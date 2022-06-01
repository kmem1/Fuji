package com.clownteam.profile_interactors.mappers

import com.clownteam.profile_datasource.network.models.ProfilesResponseItem
import com.clownteam.profile_domain.ProfileData

object ProfileDataMapper {

    fun map(profileItem: ProfilesResponseItem): ProfileData {
        return ProfileData(
            username = profileItem.username ?: "",
            avatarUrl = profileItem.avatarUrl
        )
    }
}