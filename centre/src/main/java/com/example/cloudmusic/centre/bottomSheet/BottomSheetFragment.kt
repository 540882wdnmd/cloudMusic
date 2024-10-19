package com.example.cloudmusic.centre.bottomSheet

import android.app.Dialog
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.cloudmusic.centre.MusicBinderManager
import com.example.cloudmusic.centre.R
import com.example.cloudmusic.centre.databinding.FragmentBottomSheetBinding
import com.example.cloudmusic.utils.MediaPlayerManager
import com.example.cloudmusic.utils.convertTimeFormat
import com.example.cloudmusic.utils.songArtistString
import com.example.cloudmusic.utils.webs.bean.response.Artist
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Timer
import java.util.TimerTask

class BottomSheetFragment : BottomSheetDialogFragment(){

    private val TAG = "BottomSheetFragment"

    private val mBinder = MusicBinderManager.getMusicBinder()
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var dialog: Dialog
    private lateinit var binding : FragmentBottomSheetBinding
    private lateinit var songPic : ImageView
    private lateinit var songName : TextView
    private lateinit var songArtist : TextView
    private lateinit var seekBar: SeekBar
    private lateinit var durationPlayed : TextView
    private lateinit var durationSong : TextView
    private lateinit var lastButton: Button
    private lateinit var switchButton: Button
    private lateinit var nextButton: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState)
        Log.d(TAG,"执行onCreateDialog方法")
        binding = FragmentBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        with(binding){
            songPic = songPlayingPic
            songName = songPlayingName
            songArtist = songPlayingArtist
            seekBar = musicPlayingSeekBar
            durationPlayed = durationAlreadyPlay
            durationSong = durationPlay
            lastButton = musicPlayingLast
            switchButton = musicPlayingStartStop
            nextButton = musicPlayingNext
        }
        mediaPlayer = MediaPlayerManager.getMediaPlayer()
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"执行onCreate方法")
    }

    override fun onPause() {
        super.onPause()
        mBinder.getPlayingSongData()
    }

    override fun onStart() {
        Log.d(TAG,"执行onStart方法")
        super.onStart()
        //拿到系统的 bottom_sheet
        val view: FrameLayout = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        //设置view高度
        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        //获取behavior
        val behavior = BottomSheetBehavior.from(view)
        //设置弹出高度
        behavior.peekHeight = 3000
        //设置展开状态
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        initView()
        //seekBar
        seekBar()
    }

    private fun seekBar(){
        seekBar.max = mediaPlayer.duration
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPlayer.seekTo(progress)
                    durationPlayed.text = convertTimeFormat(mediaPlayer.currentPosition)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        Timer().schedule(object :TimerTask(){
            override fun run() {
                seekBar.progress = mediaPlayer.currentPosition
                Handler(Looper.getMainLooper()).post{
                    durationPlayed.text = convertTimeFormat(mediaPlayer.currentPosition)
                }
            }
        },0, 500)
    }

    private fun initView(){
        lastButton.setOnClickListener{
            mBinder.playLast()
        }
        switchButton.setOnClickListener {
            switchButton.setBackgroundResource(R.drawable.ico_music_start_black)
            if (mediaPlayer.isPlaying){
                mBinder.pause()
                switchButton.setBackgroundResource(R.drawable.ico_music_start_black)
            }else{
                mBinder.play()
                switchButton.setBackgroundResource(R.drawable.ico_music_pause)
            }
        }
        nextButton.setOnClickListener {
            mBinder.playNext()
        }
        mBinder.playingSongData.observe(this){
            Glide.with(this)
                .load(it.al.picUrl)
                .into(songPic)
            songName.text = it.name
            songArtist.text = songArtistString(it.ar)
            durationSong.text = convertTimeFormat(mediaPlayer.duration)
            durationPlayed.text = convertTimeFormat(mediaPlayer.currentPosition)
            if (mediaPlayer.isPlaying){
                switchButton.setBackgroundResource(R.drawable.ico_music_pause)
            }else{
                switchButton.setBackgroundResource(R.drawable.ico_music_start_black)
            }
            mBinder.changeSong(it)
        }
        mBinder.getPlayingSongData()
    }


}