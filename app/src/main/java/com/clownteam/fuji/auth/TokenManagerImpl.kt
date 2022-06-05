package com.clownteam.fuji.auth

import android.util.Log
import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.token.TokenManager
import com.clownteam.fuji.api.token.TokenService
import com.clownteam.fuji.api.token.models.get_token.GetTokenRequest
import com.clownteam.fuji.api.token.models.refresh_token.RefreshTokenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("BlockingMethodInNonBlockingContext")
class TokenManagerImpl(private val tokenService: TokenService) : TokenManager {

    override fun getToken(): String? {
        return TokenPreferences.accessToken
    }

    override fun setToken(token: String) {
        TokenPreferences.accessToken = token
    }

    override fun setRefresh(refreshToken: String) {
        TokenPreferences.refreshToken = refreshToken
    }

    override fun clearTokens() {
        TokenPreferences.accessToken = null
        TokenPreferences.refreshToken = null
    }

    override suspend fun getNewToken(username: String, password: String): NetworkResponse<String?> =
        withContext(Dispatchers.IO) {
            try {
                val request = GetTokenRequest(username = username, password = password)

                val response = tokenService.getToken(request).execute()

                if (response.isSuccessful && response.body() != null) {
                    val accessToken = response.body()?.access
                    val refreshToken = response.body()?.refresh

                    TokenPreferences.accessToken = accessToken
                    TokenPreferences.refreshToken = refreshToken

                    NetworkResponse(statusCode = response.code(), data = accessToken)
                } else {
                    NetworkResponse(statusCode = response.code(), data = null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResponse(statusCode = 0, data = null, isNetworkError = true)
            }
        }

    override suspend fun refreshToken(): NetworkResponse<String?> = withContext(Dispatchers.IO) {
        try {
            if (TokenPreferences.refreshToken == null) {
                return@withContext NetworkResponse(
                    statusCode = 0,
                    data = null
                )
            }

            val request = RefreshTokenRequest(refresh = TokenPreferences.refreshToken)

            val response = tokenService.refreshToken(request).execute()

            if (response.isSuccessful && response.body() != null) {
                val accessToken = response.body()?.access
                TokenPreferences.accessToken = accessToken

                Log.d("Kmem", "Token refreshed: $accessToken")
                NetworkResponse(statusCode = response.code(), data = accessToken)
            } else {
                NetworkResponse(statusCode = response.code(), data = null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse(statusCode = 0, data = null, isNetworkError = true)
        }
    }
}