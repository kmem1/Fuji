package com.clownteam.fuji

import android.app.Application
import android.content.res.Configuration
import com.chibatching.kotpref.Kotpref
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class FujiApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Kotpref.init(this)
    }
}