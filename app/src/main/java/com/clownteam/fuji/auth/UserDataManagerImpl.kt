package com.clownteam.fuji.auth

import com.clownteam.core.user_data.UserDataManager

class UserDataManagerImpl : UserDataManager {

    override fun setUserPath(path: String) {
        UserDataPreferences.path = path
    }

    override fun getUserPath(): String? = UserDataPreferences.path

    override fun clearData() {
        UserDataPreferences.clear()
    }
}