package com.example.cloudmusic.utils.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cloudmusic.utils.ERROR
import com.example.cloudmusic.utils.OK
import com.example.cloudmusic.utils.TAG
import com.example.cloudmusic.utils.webs.service.MusicUrlService
import kotlin.properties.Delegates


class MusicServiceOnBind : Service() , MediaPlayer.OnCompletionListener {

    private val mediaPlayer = MediaPlayer()
    private var currentPosition = 0
    private val _musicUrlList = MutableLiveData<List<String>?>()
    private var mediaStatus = OK
    init {
        Log.d(TAG,"init")
        mediaStatus=getMediaStatus()
        Log.d(TAG,"播放器状态$mediaStatus")
    }

    override fun onCompletion(p0: MediaPlayer?) {
        Log.d(TAG,"执行onCompletion方法")
        if(playNext()){
            return
        }else{
            Log.e(TAG,"onCompletion方法出错")
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"执行onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"执行onStartCommend")
        return START_STICKY_COMPATIBILITY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"执行onDestroy")
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onBind(p0: Intent?): IBinder {
        Log.d(TAG,"执行onBind方法")
        return MusicBind()
    }

    inner class MusicBind : Binder(){
        val musicUrlList : LiveData<List<String>?>
            get() = _musicUrlList
        fun start() = this@MusicServiceOnBind.start()
        fun play() = this@MusicServiceOnBind.play()
        fun pause() = this@MusicServiceOnBind.pause()
        fun playNext() = this@MusicServiceOnBind.playNext()
        fun playLast() = this@MusicServiceOnBind.playLast()
        fun getPlayListLength() = this@MusicServiceOnBind.getPlayListLength()
        fun getCurrentPlayPosition() = this@MusicServiceOnBind.getCurrentPlayPosition()
        fun getMediaStatus() = this@MusicServiceOnBind.getMediaStatus()
        fun updatePlayList(newList: List<String>,position: Int) = this@MusicServiceOnBind.updatePlayList(newList,position)
    }

    private fun start(){
        Log.d(TAG,"执行start方法")
        if (getPlayListLength()==0){
            Log.e(TAG,"播放资源为空,关闭服务")
            stopSelf()
        }else{
            if (_musicUrlList.value!=null){
                Log.d(TAG,"当前是第${currentPosition}在播放")
                mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition])
                mediaPlayer.setOnCompletionListener(this)
                mediaPlayer.prepare()
                mediaPlayer.start()
                Log.d(TAG,"音乐播放状态${mediaPlayer.isPlaying}")
            }else{
                Log.e(TAG,"start方法出错")
            }
        }
    }

    private fun pause():Boolean{
        Log.d(TAG,"执行pause方法")
        if (mediaPlayer.isPlaying){
            mediaPlayer.pause()
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
            return true
        }else{
            Log.e(TAG,"歌曲没有处于暂停状态")
            return false
        }
    }

    private fun playNext():Boolean{
        Log.d(TAG,"执行playNext方法")
        if (mediaStatus==OK){
            mediaPlayer.stop()
            mediaPlayer.reset()
            if (_musicUrlList.value!=null){
                if (getCurrentPlayPosition()==getPlayListLength()){
                    currentPosition=1
                    mediaPlayer.setDataSource(_musicUrlList.value!![0])
                }else{
                    mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition++])
                }
            }else{
                Log.e(TAG,"playNext方法出错")
            }
            return true
        }else{
            return false
        }
    }

    private fun playLast():Boolean{
        Log.d(TAG,"执行playLast方法")
        if (mediaStatus==OK){
            mediaPlayer.stop()
            mediaPlayer.reset()
            if (_musicUrlList.value!=null){
                if (getCurrentPlayPosition()==1){
                    currentPosition=getPlayListLength()
                    mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition-1])
                }else{
                    mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition--])
                }
            }else{
                Log.e(TAG,"playLast方法出错")
            }
            return true
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

    private fun getCurrentPlayPosition() :Int{
        Log.d(TAG,"执行getCurrentPlayPosition方法")
        if (currentPosition>getPlayListLength()){
            Log.e(TAG,"歌曲长度错误")
            return 0;
        }else{
            return currentPosition
        }
    }

    private fun getMediaStatus(): Int{
        Log.d(TAG,"执行getMediaStatus方法")
        return if (getCurrentPlayPosition()!=0){
            OK
        }else{
            ERROR
        }
    }

    private fun updatePlayList(newPlayList : List<String>,position: Int){
        Log.d(TAG,"执行updatePlayList方法")
        _musicUrlList.value = newPlayList
        currentPosition = position
    }


}