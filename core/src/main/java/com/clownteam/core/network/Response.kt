package com.clownteam.core.network


data class NetworkResponse<T>(
    val statusCode: Int = 0,
    val data: T?,
    val isNetworkError: Boolean = false,
    val errorMessage: String = ""
) {
    val isSuccessCode: Boolean
        get() = statusCode in 200..299
}
