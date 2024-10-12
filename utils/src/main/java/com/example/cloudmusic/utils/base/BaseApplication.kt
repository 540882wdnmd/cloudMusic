package com.example.cloudmusic.utils.base

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cloudmusic.utils.getCookie
import java.lang.ref.WeakReference

class BaseApplication : Application() {

    companion object {
        lateinit var appContext: Application
        lateinit var activity: WeakReference<AppCompatActivity>
        lateinit var mediaPlayer: MediaPlayer
        var cookie: String = ""
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("BaseApplication","onCreate方法")
        appContext = this@BaseApplication
        mediaPlayer = MediaPlayer()
        cookie = getCookie()
    }
}