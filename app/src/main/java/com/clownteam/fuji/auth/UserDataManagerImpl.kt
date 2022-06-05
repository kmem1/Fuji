package com.clownteam.fuji.auth

import com.clownteam.core.user_data.UserDataManager

class UserDataManagerImpl : UserDataManager {

    override fun setUsername(username: String) {
        UserDataPreferences.username = username
    }

    override fun getUsername(): String? = UserDataPreferences.username

    override fun clearData() {
        UserDataPreferences.clear()
    }
}