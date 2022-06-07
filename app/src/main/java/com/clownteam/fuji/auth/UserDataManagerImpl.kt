package com.clownteam.fuji.auth

import com.clownteam.core.user_data.UserDataManager

class UserDataManagerImpl : UserDataManager {

    override fun setUserPath(path: String) {
        UserDataPreferences.username = path
    }

    override fun getUserPath(): String? = UserDataPreferences.username

    override fun clearData() {
        UserDataPreferences.clear()
    }
}