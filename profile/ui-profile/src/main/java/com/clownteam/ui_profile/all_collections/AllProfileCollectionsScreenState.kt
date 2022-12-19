package com.clownteam.ui_profile.all_collections

import com.clownteam.components.UiText
import com.clownteam.profile_domain.ProfileCollection

data class AllProfileCollectionsScreenState(
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val isUnauthorized: Boolean = false,
    val collections: List<ProfileCollection> = emptyList()
)