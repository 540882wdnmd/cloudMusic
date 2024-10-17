//package com.example.cloudmusic.utils.service
//
//import android.app.Notification
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.app.Service
//import android.os.Build
//import android.content.Context
//import android.content.Intent
//import android.widget.RemoteViews
//import androidx.core.app.NotificationCompat
//
//class NotificationService : Service() {
//
//    companion object{
//        val instance : Notification by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
//            Notification()
//        }
//        const val MUSIC_NOTIFICATION_ACTION_PLAY = "MusicNotificationToPLAY"
//        const val MUSIC_NOTIFICATION_ACTION_NEXT = "MusicNotificationToNEXT"
//        const val MUSIC_NOTIFICATION_ACTION_CLOSE = "MusicNotificationToCLOSE"
//        const val MUSIC_NOTIFICATION_INTENT_KEY = "type"
//        const val MUSIC_NOTIFICATION_VALUE_PLAY = 30001
//        const val MUSIC_NOTIFICATION_VALUE_NEXT = 30002
//        const val MUSIC_NOTIFICATION_VALUE_CLOSE =30003
//        const val REQUEST_CODE = 3000
//        const val NOTIFICATION_ID = 10001
//    }
//
//
//
//    private lateinit var  musicNotification : Notification
//    private lateinit var notificationManager : NotificationManager
//    private lateinit var builder : Builder()
//    private lateinit var context: Context
//    private lateinit var remoteViews: RemoteViews
//
//
//    private val playIntent = Intent()
//    private val pauseIntent = Intent()
//    private val lastIntent = Intent()
//    private val nextIntent = Intent()
//    private lateinit var musicPendIntent : PendingIntent
//
//
//    fun setManager(manager: NotificationManager){
//        this.notificationManager = manager
//    }
//
//    fun setContext(context: Context){
//        this.context=context
//    }
//
//    fun initNotification(){
//        remoteViews = RemoteViews(p,R.layout.notification_music)
//    }
//
//}