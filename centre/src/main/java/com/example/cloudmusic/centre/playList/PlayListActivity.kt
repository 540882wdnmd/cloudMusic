package com.example.cloudmusic.centre.playList

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudmusic.centre.MusicBinderManager
import com.example.cloudmusic.centre.databinding.ActivityPlayListBinding
import com.example.cloudmusic.utils.MediaPlayerManager
import com.example.cloudmusic.utils.PERSONALIZED_ID
import com.example.cloudmusic.utils.TAG
import com.example.cloudmusic.utils.base.BaseApplication
import com.example.cloudmusic.utils.toast
import com.example.cloudmusic.utils.webs.bean.response.Song

class PlayListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayListBinding
    private lateinit var toolbar: Toolbar
    private lateinit var playListAdapter: PlayListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mediaPlayer: MediaPlayer

    private val mBinder = MusicBinderManager.getMusicBinder()
    private lateinit var playListID : String
    private var songIds = StringBuilder()
    private val musicUrls = ArrayList<String>()
    private val songList = ArrayList<Song>()


    private val playListViewModel by lazy { ViewModelProvider(this)[PlayListViewModel::class] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mediaPlayer = MediaPlayerManager.getMediaPlayer()
        with(binding){
            toolbar = toolbarPlayList
            recyclerView = recyclerViewPlayList
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setRecyclerView()
        setMusicUrlObserve()
        getPlayListID()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"执行onDestroy方法")
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setRecyclerView(){
        playListAdapter = PlayListAdapter(null)
        recyclerView.adapter = playListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        playListAdapter.setOnClickListener {
            position->
            Log.d(TAG,"点击了第${position}个item")
            serviceLogic(position)
        }
    }

    private fun getPlayListID() {
        playListID = intent.getStringExtra(PERSONALIZED_ID) ?: ""
        Log.d(TAG,playListID)
        playListViewModel.playList.observe(this){
            if (it!=null){
                if (it.code==200){
                    playListAdapter.updateData(it.songs)
                    for (song in it.songs) {
                        songIds.append(song.id).append(",")
                        songList.add(song)
                    }
                    songIds.deleteCharAt(songIds.length-1)
                    Log.d(TAG, "歌单歌曲的所有ID:$songIds")
                    playListViewModel.getMusicUrl(songIds.toString())
                }else if(it.code == 400){
                    it.msg?.let { it1 -> toast(it1) }
                }
            }else{
                toast("网络连接错误！！！")
            }
        }
        playListViewModel.getPlayList(playListID)
    }

    private fun setMusicUrlObserve(){
        playListViewModel.musicUrl.observe(this){
            if (it!=null){
                if (it.code == 200){
                    for (url in it.data){
                        musicUrls.add(url.url)
                    }
                    Log.d(TAG,"歌单音乐Url:$musicUrls")
                }else if (it.code == 400){
                    it.msg?.let { it1 -> toast(it1) }
                }
            }else{
                toast("网络连接错误！！！")
            }
        }
    }

    private fun serviceLogic(position: Int){
        mBinder.musicUrlList.observe(this){
            Log.d(TAG,"服务中的musicUrl发生变化")
            with(mediaPlayer){
                if (this.isPlaying){
                    pause()
                    stop()
                }
                reset()
            }
            mBinder.start()
        }
        mBinder.playingSongData.observe(this){
            mBinder.changeSong(it)
        }
        mBinder.updatePlayList(musicUrls.toList(),position)
        mBinder.sendPlayListData(songList)
    }



}