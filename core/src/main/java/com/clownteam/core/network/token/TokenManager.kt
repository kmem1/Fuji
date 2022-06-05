package com.clownteam.core.network.token

import com.clownteam.core.network.NetworkResponse

interface TokenManager {

    fun getToken(): String?
    fun setToken(token: String)
    fun setRefresh(refreshToken: String)

    fun clearTokens()

    suspend fun getNewToken(username: String, password: String): NetworkResponse<String?>
    suspend fun refreshToken(): NetworkResponse<String?>
}