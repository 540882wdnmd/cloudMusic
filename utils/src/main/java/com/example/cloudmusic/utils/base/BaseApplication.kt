package com.example.cloudmusic.utils.base

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import com.example.cloudmusic.utils.MediaPlayerManager
import com.example.cloudmusic.utils.getCookie
import com.example.cloudmusic.utils.service.MusicServiceOnBind

class BaseApplication : Application() {

    companion object {
        lateinit var appContext: Application
        lateinit var mBinder : MusicServiceOnBind.MusicBind
        var cookie: String? = ""
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("BaseApplication","onCreate方法")
        appContext = this@BaseApplication
        MediaPlayerManager.init(appContext)
        cookie = getCookie()
    }


}