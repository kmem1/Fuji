package com.clownteam.profile_datasource.network

import com.clownteam.core.network.NetworkResponse
import com.clownteam.profile_datasource.network.models.get_collections.GetUserCollectionsResponse
import com.clownteam.profile_datasource.network.models.get_profile.ProfileResponse
import com.clownteam.profile_datasource.network.models.get_user_courses.GetUserCoursesResponse

interface ProfileService {

    suspend fun getProfileData(token: String): NetworkResponse<ProfileResponse>

    suspend fun getUserCourses(
        token: String,
        userPath: String
    ): NetworkResponse<GetUserCoursesResponse>

    suspend fun getUserCollections(
        token: String,
        userPath: String
    ): NetworkResponse<GetUserCollectionsResponse>
}