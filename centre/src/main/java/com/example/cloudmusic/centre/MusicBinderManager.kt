package com.example.cloudmusic.centre

import android.os.IBinder

object MusicBinderManager {

    private var mBinder : MusicServiceOnBind.MusicBind? = null

    fun init(service : IBinder?){
        if (mBinder==null){
            mBinder = service as MusicServiceOnBind.MusicBind
        }
    }

    fun getMusicBinder(): MusicServiceOnBind.MusicBind {
        return mBinder!!
    }

    fun unbind(){
        if (mBinder!=null){
           mBinder=null
        }
    }
}