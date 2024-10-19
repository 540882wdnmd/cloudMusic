package com.example.cloudmusic.utils.base

import android.app.Application
import android.util.Log
import com.example.cloudmusic.utils.MediaPlayerManager

class BaseApplication : Application() {

    companion object {
        lateinit var appContext: Application
        var cookie: String? = ""
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("BaseApplication","onCreate方法")
        appContext = this@BaseApplication
        MediaPlayerManager.init(appContext)
    }


}