package com.clownteam.core.network

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