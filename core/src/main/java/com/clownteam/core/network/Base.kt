package com.clownteam.core.network

import com.clownteam.core.network.token.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun <T> baseRequest(request: () -> Call<T>): NetworkResponse<T> =
    withContext(Dispatchers.IO) {
        try {
            val apiResponse = request().execute()

            NetworkResponse(statusCode = apiResponse.code(), data = apiResponse.body())
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse(isNetworkError = true, statusCode = 0, data = null)
        }
    }

suspend fun <T> authorizationRequest(
    tokenManager: TokenManager,
    apiCall: suspend (token: String) -> NetworkResponse<T>
): NetworkResponse<T> = withContext(Dispatchers.IO) {
    val token = tokenManager.getToken() ?: return@withContext NetworkResponse.unauthorized()

    var result = apiCall(token)

    if (result.statusCode == 401) {
        val newTokenResponse = tokenManager.refreshToken()

        if (newTokenResponse.isNetworkError) {
            return@withContext NetworkResponse.networkError()
        }

        if (newTokenResponse.isSuccessCode && newTokenResponse.data != null) {
            result = apiCall(newTokenResponse.data)
        } else {
            tokenManager.clearTokens()
            return@withContext NetworkResponse.unauthorized()
        }
    }

    return@withContext result
}