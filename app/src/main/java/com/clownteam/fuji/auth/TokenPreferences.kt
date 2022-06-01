package com.clownteam.fuji.auth

import com.chibatching.kotpref.KotprefModel

object TokenPreferences : KotprefModel() {
    var accessToken by nullableStringPref()
    var refreshToken by nullableStringPref()
}