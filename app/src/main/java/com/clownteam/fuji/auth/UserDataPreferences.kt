package com.clownteam.fuji.auth

import com.chibatching.kotpref.KotprefModel

object UserDataPreferences : KotprefModel() {
    var path by nullableStringPref()
}