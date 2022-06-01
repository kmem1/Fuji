package com.clownteam.fuji

import android.app.Application
import com.chibatching.kotpref.Kotpref
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FujiApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Kotpref.init(this)
    }
}