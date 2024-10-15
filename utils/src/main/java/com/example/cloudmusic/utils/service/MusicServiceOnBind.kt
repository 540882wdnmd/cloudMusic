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
import com.example.cloudmusic.utils.base.BaseApplication
import com.example.cloudmusic.utils.webs.bean.response.Song
import com.example.cloudmusic.utils.webs.service.MusicUrlService
import kotlin.properties.Delegates


class MusicServiceOnBind : Service() , MediaPlayer.OnCompletionListener {

    private val mediaPlayer = BaseApplication.mediaPlayer
    private var currentPosition = -1
    private val _musicUrlList = MutableLiveData<List<String>?>()
    private val _playingSongData = MutableLiveData<Song>()
    private val playListData = ArrayList<Song?>()
    private var mediaStatus = OK
    init {
        Log.d(TAG,"init")
        mediaStatus=getMediaStatus()
        mediaPlayer.setOnCompletionListener(this)
        Log.d(TAG,"播放器状态$mediaStatus")
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
        Log.d(TAG,"执行onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"执行onStartCommend")
        return START_STICKY_COMPATIBILITY
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
        fun getPlayListLength() = this@MusicServiceOnBind.getPlayListLength()
        fun getCurrentPlayPosition() = this@MusicServiceOnBind.getCurrentPlayPosition()
        fun getMediaStatus() = this@MusicServiceOnBind.getMediaStatus()
        fun updatePlayList(newList: List<String>,position: Int) = this@MusicServiceOnBind.updatePlayList(newList,position)
        fun sendPlayListData(data : List<Song>) = this@MusicServiceOnBind.sendPlayListData(data)
        fun getPlayingSongData() = this@MusicServiceOnBind.getPlayingSongData()
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
                    playNext()
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
            mediaPlayer.pause()
            mediaPlayer.stop()
            mediaPlayer.reset()
            if (_musicUrlList.value!=null){
                try {
                    if (getCurrentPlayPosition()==getPlayListLength()-1){
                        currentPosition=0
                        mediaPlayer.setDataSource(_musicUrlList.value!![0])
                    }else{
                        currentPosition++
                        mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition])
                    }
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    _playingSongData.value = playListData[currentPosition]
                }catch (e:Throwable){
                    Log.e(TAG,e.message,e)
                    playNext()
                }
                return true
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
        if (mediaStatus==OK){
            mediaPlayer.pause()
            mediaPlayer.stop()
            mediaPlayer.reset()
            if (_musicUrlList.value!=null){
                try {
                    if (getCurrentPlayPosition()==0){
                        currentPosition=getPlayListLength()-1
                        mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition])
                    }else{
                        currentPosition--
                        mediaPlayer.setDataSource(_musicUrlList.value!![currentPosition])
                    }
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    _playingSongData.value = playListData[currentPosition]
                }catch (e: Throwable){
                    Log.e(TAG,e.message,e)
                    playLast()
                }
            }else{
                Log.e(TAG,"playLast方法出错")
            }
            Log.d(TAG,"当前是第${currentPosition}在播放")
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
        if (currentPosition>getPlayListLength()-1){
            Log.e(TAG,"歌曲长度错误")
            return -1;
        }else{
            return currentPosition
        }
    }

    private fun getMediaStatus(): Int{
        Log.d(TAG,"执行getMediaStatus方法")
        return if (getCurrentPlayPosition()!=-1){
            OK
        }else{
            ERROR
        }
    }

    private fun updatePlayList(newPlayList : List<String>,position: Int){
        Log.d(TAG,"执行updatePlayList方法")
        currentPosition = position
        _musicUrlList.value = newPlayList
        mediaStatus = getMediaStatus()
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

}