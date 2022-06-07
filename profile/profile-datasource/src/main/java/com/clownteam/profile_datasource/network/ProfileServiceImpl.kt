package com.clownteam.profile_datasource.network

import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.baseRequest
import com.clownteam.profile_datasource.network.models.ProfileResponse

@Suppress("BlockingMethodInNonBlockingContext")
class ProfileServiceImpl(private val api: ProfileApi) : ProfileService {

    override suspend fun getProfileData(token: String): NetworkResponse<ProfileResponse> =
        baseRequest { api.getProfile("Bearer $token") }

}
