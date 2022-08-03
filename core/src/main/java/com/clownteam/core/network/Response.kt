package com.clownteam.core.network


data class NetworkResponse<T>(
    val statusCode: Int = 0,
    val data: T? = null,
    val isNetworkError: Boolean = false,
    val errorMessage: String = ""
) {
    val isSuccessCode: Boolean
        get() = statusCode in 200..299

    val isUnauthorized: Boolean
        get() = statusCode == 401

    companion object {
        fun <T> unauthorized(): NetworkResponse<T> = NetworkResponse(statusCode = 401)

        fun <T> networkError(): NetworkResponse<T> = NetworkResponse(isNetworkError = true)
    }

}
