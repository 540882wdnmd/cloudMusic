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
        mediaStatus=getMediaStatus()
    }

    override fun onCompletion(p0: MediaPlayer?) {
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
        if (getPlayListLength()==0){
            Log.e(TAG,"播放资源为空,关闭服务")
            stopSelf()
        }else{
            if (_musicUrlList.value!=null){
                mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition])
                mediaPlayer.setOnCompletionListener(this)
            }else{
                Log.e(TAG,"onStartCommand出错")
            }
        }
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
        return MusicBind()
    }

    class MusicBind : Binder(){
        companion object{
            val service  = MusicServiceOnBind()
        }
        val musicUrlList : LiveData<List<String>?>
            get() = service._musicUrlList
        fun play() = service.play()
        fun pause() = service.pause()
        fun playNext() = service.playNext()
        fun playLast() = service.playLast()
        fun getPlayListLength() = service.getPlayListLength()
        fun getCurrentPlayPosition() = service.getCurrentPlayPosition()
        fun getMediaStatus() = service.getMediaStatus()
        fun updatePlayList(newList: List<String>,position: Int) = service.updatePlayList(newList,position)
    }

    private fun pause():Boolean{
        if (mediaPlayer.isPlaying){
            mediaPlayer.pause()
            return true
        }else{
            Log.e(TAG,"歌曲没有处于播放状态")
            return false
        }
    }

    private fun play():Boolean{
        if (!mediaPlayer.isPlaying){
            mediaPlayer.start()
            return true
        }else{
            Log.e(TAG,"歌曲没有处于暂停状态")
            return false
        }
    }

    private fun playNext():Boolean{
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

    private fun getPlayListLength()= _musicUrlList.value?.size ?: 0

    private fun getCurrentPlayPosition() :Int{
        if (currentPosition>getPlayListLength()){
            Log.e(TAG,"歌曲长度错误")
            return 0;
        }else{
            return currentPosition
        }
    }

    private fun getMediaStatus(): Int{
        return if (getCurrentPlayPosition()!=0){
            OK
        }else{
            ERROR
        }
    }

    private fun updatePlayList(newPlayList : List<String>,position: Int){
        _musicUrlList.value = newPlayList
        currentPosition = position
    }


}