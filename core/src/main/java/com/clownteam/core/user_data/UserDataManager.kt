package com.clownteam.core.user_data

interface UserDataManager {
    fun setUserPath(path: String)
    fun getUserPath(): String?

    fun clearData()
}