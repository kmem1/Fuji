package com.clownteam.ui_profile

import com.clownteam.core.domain.SResult
import com.clownteam.profile_domain.ProfileCollection
import com.clownteam.profile_domain.ProfileCourse
import com.clownteam.profile_domain.ProfileData

data class ProfileState(
    val profileData: SResult<ProfileData> =
        SResult.Success(ProfileData("", null, null)),
    val isNetworkError: Boolean = false,
    val isUnauthorized: Boolean = false,
    val profileCourses: SResult<List<ProfileCourse>> = SResult.Success(emptyList()),
    val profileCollections:  SResult<List<ProfileCollection>> = SResult.Success(emptyList())
)