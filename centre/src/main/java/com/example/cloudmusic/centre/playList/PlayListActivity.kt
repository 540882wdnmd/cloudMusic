package com.example.cloudmusic.centre.playList

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudmusic.centre.R
import com.example.cloudmusic.centre.databinding.ActivityPlayListBinding
import com.example.cloudmusic.utils.PERSONALIZED_ID
import com.example.cloudmusic.utils.TAG
import com.example.cloudmusic.utils.service.MusicServiceOnBind
import com.example.cloudmusic.utils.toast

class PlayListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayListBinding
    private lateinit var toolbar: Toolbar
    private lateinit var playListAdapter: PlayListAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var playListID : String
    private var songIds = StringBuilder()
    private val musicUrls = ArrayList<String>()

    private val playListViewModel by lazy { ViewModelProvider(this)[PlayListViewModel::class] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            toolbar = toolbarPlayList
            recyclerView = recyclerViewPlayList
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setRecyclerView()
        getPlayListID()
        getMusicUrl()
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
                    }
                    songIds.deleteCharAt(songIds.length-1)
                }else if(it.code == 400){
                    it.msg?.let { it1 -> toast(it1) }
                }
            }else{
                toast("网络连接错误！！！")
            }
        }
        playListViewModel.getPlayList(playListID)
    }

    private fun getMusicUrl(){
        playListViewModel.musicUrl.observe(this){
            if (it!=null){
                if (it.code == 200){
                    for (url in it.data){
                        musicUrls.add(url.url)
                    }
                }else if (it.code == 400){
                    it.msg?.let { it1 -> toast(it1) }
                }
            }else{
                toast("网络连接错误！！！")
            }
        }
        playListViewModel.getMusicUrl(songIds.toString())
    }

    private fun startService(){
        val serviceConnection = object : ServiceConnection{
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                val mBinder = p1 as MusicServiceOnBind.MusicBind
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                return
            }

        }
    }

}