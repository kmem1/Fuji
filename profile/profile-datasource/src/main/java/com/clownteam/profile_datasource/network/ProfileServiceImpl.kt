package com.clownteam.profile_datasource.network

import com.clownteam.core.network.NetworkResponse
import com.clownteam.profile_datasource.network.models.ProfilesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("BlockingMethodInNonBlockingContext")
class ProfileServiceImpl(private val api: ProfileApi) : ProfileService {

    override suspend fun getProfileData(token: String): NetworkResponse<ProfilesResponse> =
        withContext(Dispatchers.IO) {
            try {
                val apiResponse = api.getProfiles("Bearer $token").execute()

                NetworkResponse(statusCode = apiResponse.code(), data = apiResponse.body())
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResponse(isNetworkError = true, statusCode = 0, data = null)
            }
        }
}