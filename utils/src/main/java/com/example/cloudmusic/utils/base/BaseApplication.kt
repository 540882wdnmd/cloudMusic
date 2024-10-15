package com.example.cloudmusic.utils.base

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cloudmusic.utils.getCookie
import com.example.cloudmusic.utils.service.MusicServiceOnBind
import java.lang.ref.WeakReference

class BaseApplication : Application() {

    companion object {
        lateinit var appContext: Application
        lateinit var activity: WeakReference<AppCompatActivity>
        lateinit var mediaPlayer: MediaPlayer
        lateinit var mBinder : MusicServiceOnBind.MusicBind
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