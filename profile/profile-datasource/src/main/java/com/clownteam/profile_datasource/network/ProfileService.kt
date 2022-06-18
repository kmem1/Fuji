package com.clownteam.profile_datasource.network

import com.clownteam.core.network.NetworkResponse
import com.clownteam.profile_datasource.network.models.ProfileResponse

interface ProfileService {

    suspend fun getProfileData(token: String): NetworkResponse<ProfileResponse>
}