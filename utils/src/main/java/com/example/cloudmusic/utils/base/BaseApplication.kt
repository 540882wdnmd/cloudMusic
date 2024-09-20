package com.example.cloudmusic.utils.base

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.example.cloudmusic.utils.getCookie
import java.lang.ref.WeakReference

class BaseApplication : Application() {

    companion object {
        lateinit var appContext: Application
        lateinit var activity: WeakReference<AppCompatActivity>
        var cookie: String = ""
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        cookie = getCookie()
    }
}