package com.example.cloudmusic.centre

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.cloudmusic.utils.BroadcastMsg
import com.example.cloudmusic.utils.MediaPlayerManager
import com.example.cloudmusic.utils.TAG
import com.example.cloudmusic.utils.songArtistString
import com.example.cloudmusic.utils.webs.bean.response.Song


class MusicServiceOnBind : Service() , MediaPlayer.OnCompletionListener {

    private lateinit var mediaPlayer : MediaPlayer
    private var currentPosition = -1
    private val _musicUrlList = MutableLiveData<List<String>?>()
    private val _playingSongData = MutableLiveData<Song>()
    private val playListData = ArrayList<Song?>()
    private lateinit var remoteViews: RemoteViews

    companion object{
        const val MUSIC_NOTIFICATION_ACTION_LAST = "MusicNotificationToLast"
        const val MUSIC_NOTIFICATION_ACTION_NEXT = "MusicNotificationToNEXT"
        const val MUSIC_NOTIFICATION_ACTION_SWITCH = "MusicNotificationToSwitch"
        const val NOTIFICATION_CHANNEL_NAME = "MusicChannel"
        const val CHANNEL_ID = "cloud.music"
        const val NOTIFICATION_ID = 10001
    }

    init {
        Log.d(TAG,"init")
    }

    override fun onCompletion(p0: MediaPlayer?) {
        Log.d(TAG,"执行onCompletion方法")
        if(playNext()){
            Log.d(TAG,"当前是第${currentPosition}首歌")
            return
        }else{
            Log.e(TAG,"onCompletion方法出错")
        }
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayerManager.getMediaPlayer()
        mediaPlayer.setOnCompletionListener(this)
        remoteViews = RemoteViews(packageName,R.layout.notification_music)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                createNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        }
        Log.d(TAG,"执行onCreate")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG,"执行onStartCommend")
        when(intent.action){
            MUSIC_NOTIFICATION_ACTION_SWITCH->{
                Log.d(TAG,MUSIC_NOTIFICATION_ACTION_SWITCH)
                if (mediaPlayer.isPlaying){
                    sendBroadcast(BroadcastMsg.PAUSE)
                }else{
                    sendBroadcast(BroadcastMsg.PLAY)
                }
            }

            MUSIC_NOTIFICATION_ACTION_LAST->{
                Log.d(TAG, MUSIC_NOTIFICATION_ACTION_LAST)
                sendBroadcast(BroadcastMsg.LAST)
            }

            MUSIC_NOTIFICATION_ACTION_NEXT->{
                Log.d(TAG, MUSIC_NOTIFICATION_ACTION_NEXT)
                sendBroadcast(BroadcastMsg.NEXT)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"执行onDestroy")
    }

    override fun onBind(p0: Intent?): IBinder {
        Log.d(TAG,"执行onBind方法")
        return MusicBind()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG,"执行onUnbind方法")
        return super.onUnbind(intent)
    }

    inner class MusicBind : Binder(){
        val musicUrlList : LiveData<List<String>?>
            get() = _musicUrlList
        val playingSongData : LiveData<Song>
            get() = _playingSongData
        fun start() = this@MusicServiceOnBind.start()
        fun play() = this@MusicServiceOnBind.play()
        fun pause() = this@MusicServiceOnBind.pause()
        fun playNext() = this@MusicServiceOnBind.playNext()
        fun playLast() = this@MusicServiceOnBind.playLast()
        fun updatePlayList(newList: List<String>,position: Int) = this@MusicServiceOnBind.updatePlayList(newList,position)
        fun sendPlayListData(data : List<Song>) = this@MusicServiceOnBind.sendPlayListData(data)
        fun getPlayingSongData() = this@MusicServiceOnBind.getPlayingSongData()
        fun changeSong(song : Song?) = this@MusicServiceOnBind.changeSong(song)
        fun setRemoteViewSwitch(isPlay : Boolean) = this@MusicServiceOnBind.setRemoteViewSwitch(isPlay)
    }

    private fun start(){
        Log.d(TAG,"执行start方法")
        if (getPlayListLength()==0){
            Log.e(TAG,"播放资源为空,关闭服务")
            stopSelf()
        }else{
            if (_musicUrlList.value!=null){
                try {
                    mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition])
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    Log.d(TAG,"当前是第${currentPosition}在播放")
                }catch (e:Throwable){
                    Log.e(TAG,e.message,e)
                }
            }else{
                Log.e(TAG,"start方法出错")
            }
        }
    }

    private fun pause():Boolean{
        Log.d(TAG,"执行pause方法")
        if (mediaPlayer.isPlaying){
            mediaPlayer.pause()
            setRemoteViewSwitch(mediaPlayer.isPlaying)
            return true
        }else{
            Log.e(TAG,"歌曲没有处于播放状态")
            return false
        }
    }

    private fun play():Boolean{
        Log.d(TAG,"执行play方法")
        if (!mediaPlayer.isPlaying){
            mediaPlayer.start()
            setRemoteViewSwitch(mediaPlayer.isPlaying)
            return true
        }else{
            Log.e(TAG,"歌曲没有处于暂停状态")
            return false
        }
    }

    private fun playNext():Boolean{
        Log.d(TAG,"执行playNext方法")
        if (currentPosition!=-1){
            mediaPlayer.pause()
            mediaPlayer.stop()
            mediaPlayer.reset()
            if (_musicUrlList.value!=null){
                try {
                    if (currentPosition==getPlayListLength()-1){
                        currentPosition=0
                        mediaPlayer.setDataSource(_musicUrlList.value!![0])
                    }else{
                        currentPosition++
                        mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition])
                    }
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    _playingSongData.value = playListData[currentPosition]
                    Log.d(TAG,"当前是第${currentPosition}在播放")
                    return true
                }catch (e:Throwable){
                    Log.e(TAG,e.message,e)
                    return false
                }
            }else{
                Log.e(TAG,"playNext方法出错")
                return false
            }
        }else{
            return false
        }
    }

    private fun playLast():Boolean{
        Log.d(TAG,"执行playLast方法")
        if (currentPosition!=-1){
            mediaPlayer.pause()
            mediaPlayer.stop()
            mediaPlayer.reset()
            if (_musicUrlList.value!=null){
                try {
                    if (currentPosition==0){
                        currentPosition=getPlayListLength()-1
                        mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition])
                    }else{
                        currentPosition--
                        mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition])
                    }
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    _playingSongData.value = playListData[currentPosition]
                    Log.d(TAG,"当前是第${currentPosition}在播放")
                    return true
                }catch (e: Throwable){
                    Log.e(TAG,e.message,e)
                    return false
                }

            }else{
                Log.e(TAG,"playLast方法出错")
                return false
            }
        }else{
            return false
        }
    }

    private fun getPlayListLength():Int{
        Log.d(TAG,"执行getPlayListLength方法")
        return if (_musicUrlList.value!=null){
            _musicUrlList.value!!.size
        }else{
            0
        }
    }


    private fun updatePlayList(newPlayList : List<String>,position: Int){
        Log.d(TAG,"执行updatePlayList方法")
        currentPosition = position
        _musicUrlList.value = newPlayList
    }

    private fun sendPlayListData(data : List<Song>){
        playListData.clear()
        for (d in data){
            playListData.add(d)
        }
       if (currentPosition!=-1){
           _playingSongData.value = playListData[currentPosition]
       }
    }

    private fun getPlayingSongData() : Song?{
        if (currentPosition!=-1){
            _playingSongData.value = playListData[currentPosition]
        }
        return _playingSongData.value
    }


    private fun createNotification() : Notification {
        val intentLast = Intent(this, MusicServiceOnBind::class.java)
        intentLast.setAction(MUSIC_NOTIFICATION_ACTION_LAST)
        val intentSwitch = Intent(this, MusicServiceOnBind::class.java)
        intentSwitch.setAction(MUSIC_NOTIFICATION_ACTION_SWITCH)
        val intentNext = Intent(this, MusicServiceOnBind::class.java)
        intentNext.setAction(MUSIC_NOTIFICATION_ACTION_NEXT)

        //PendingIntent.FLAG_MUTABLE:Intent的对象是可变的
        val pIntentLast = PendingIntent.getService(this, 1,intentLast,PendingIntent.FLAG_MUTABLE)
        val pIntentSwitch = PendingIntent.getService(this, 2,intentSwitch,PendingIntent.FLAG_MUTABLE)
        val pIntentNext = PendingIntent.getService(this, 3,intentNext,PendingIntent.FLAG_MUTABLE)

        remoteViews.setOnClickPendingIntent(R.id.notification_last,pIntentLast)
        remoteViews.setOnClickPendingIntent(R.id.notification_switch,pIntentSwitch)
        remoteViews.setOnClickPendingIntent(R.id.notification_next,pIntentNext)

        val channel = NotificationChannel(
            CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ico_app)
            .setCustomContentView(remoteViews)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(NOTIFICATION_ID,notification)

        return notification
    }

    private fun sendBroadcast(action: String){
        val intent = Intent(action)
        intent.setPackage(packageName)
        this.sendBroadcast(intent)
    }



    private fun changeSong(song: Song?){
        if (song!=null){
            Glide.with(this)
                .asBitmap()
                .load(song.al.picUrl)
                .override(256)
                .transform(RoundedCorners(35))
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Log.d(TAG,"refreshRemoteView方法")
                        remoteViews.setImageViewBitmap(R.id.notification_img,resource)
                        remoteViews.setTextViewText(R.id.notification_music_name,song.name)
                        remoteViews.setTextViewText(R.id.notification_artist, songArtistString(song.ar))
                        setRemoteViewSwitch(mediaPlayer.isPlaying)
                        updateRemoteView()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        Log.d(TAG,"LoadCleared")
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        Log.e(TAG,"LoadFailed")
                    }

                })

        }

    }

    private fun updateRemoteView(){
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, createNotification())
    }

    private fun setRemoteViewSwitch(isPlay : Boolean){
        if (isPlay){
            remoteViews.setImageViewResource(R.id.notification_switch,R.drawable.ico_music_pause)
        }else{
            remoteViews.setImageViewResource(R.id.notification_switch,R.drawable.ico_music_start_black)
        }
        updateRemoteView()
    }


}