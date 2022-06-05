package com.clownteam.core.user_data

interface UserDataManager {
    fun setUsername(username: String)
    fun getUsername(): String?

    fun clearData()
}