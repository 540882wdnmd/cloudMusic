package com.example.cloudmusic.utils

import android.content.Context
import android.media.MediaPlayer
import java.lang.ref.WeakReference

object MediaPlayerManager {

    private var mediaPlayer : MediaPlayer? = null
    private var contextWeakReference : WeakReference<Context>? = null

    fun init(context: Context){
        contextWeakReference = WeakReference(context)
        if (mediaPlayer==null){
            mediaPlayer=MediaPlayer()
        }
    }

    fun getMediaPlayer():MediaPlayer{
        if (mediaPlayer==null){
            contextWeakReference?.get()?.let {
                mediaPlayer = MediaPlayer()
            }
        }
        return mediaPlayer!!
    }

    fun releaseMediaPlayer(){
        mediaPlayer?.release()
        mediaPlayer = null
    }
}