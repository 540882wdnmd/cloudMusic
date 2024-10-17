package com.example.cloudmusic.centre

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.cloudmusic.centre.bottomSheet.BottomSheetFragment
import com.example.cloudmusic.centre.databinding.ActivityCentreBinding
import com.example.cloudmusic.utils.MediaPlayerManager
import com.example.cloudmusic.utils.TAG
import com.example.cloudmusic.utils.base.BaseApplication
import com.example.cloudmusic.utils.base.BaseApplication.Companion.appContext
import com.example.cloudmusic.utils.hideKeyboard
import com.example.cloudmusic.utils.service.MusicServiceOnBind
import com.google.android.material.bottomnavigation.BottomNavigationView

class CentreActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var simpleViewPic : ImageView
    private lateinit var simpleViewName : TextView
    private lateinit var simpleViewSwitch : ImageButton
    private lateinit var simpleViewNext : ImageButton
    private lateinit var simpleView : View
    private lateinit var binding : ActivityCentreBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbarMain: Toolbar
    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var navController : NavController

    private lateinit var mBinder : MusicServiceOnBind.MusicBind

    init {
        Log.d(TAG,"init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"执行onCreate")

        binding  = ActivityCentreBinding.inflate(layoutInflater)
        setContentView(binding.root)//绑定布局

        mediaPlayer = MediaPlayerManager.getMediaPlayer()
        startService()
        setDrawerLayout()
        setBottomNav()
        setSimpleView()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"执行onStart")
    }
    override fun onResume() {
        super.onResume()
        if (mediaPlayer.isPlaying){
            simpleViewSwitch.setImageResource(R.drawable.ico_music_pause)
        }else{
            simpleViewSwitch.setImageResource(R.drawable.ico_music_start_black)
        }
        Log.d(TAG,"执行onResume")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG,"执行onPause")
    }
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG,"执行onRestart")
        serviceLogic()
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG,"执行onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }
        mediaPlayer.stop()
        MediaPlayerManager.releaseMediaPlayer()
        Log.d(TAG,"执行onDestroy")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //重写这个方法可以改变按下返回键后执行的操作，因为drawer相当于一个fragment，如果不重写这个方法，那么按下返回键将会直接退出活动
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawers()
                return false
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            hideKeyboard(ev,null)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun setDrawerLayout(){
        drawerLayout = binding.drawerLayoutMain
        toolbarMain = binding.appBarMain.toolbarMain
        setSupportActionBar(toolbarMain)//设置toolbar

        //导航按钮
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarDrawerToggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
    }

    private fun setBottomNav(){
        //设置底部导航按钮
        bottomNavView = binding.appBarMain.contentMain.bottomNavView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfigurationBottom = AppBarConfiguration(
            setOf(
                R.id.navigation_recommend,
                R.id.navigation_discover,
                R.id.navigation_roam,
                R.id.navigation_dynamic,
                R.id.navigation_mine
            ),drawerLayout
        )
        setupActionBarWithNavController(navController,appBarConfigurationBottom)
        bottomNavView.setupWithNavController(navController)
    }

    private fun setSimpleView(){
        with(binding.appBarMain.contentMain.viewSimpleMusicPlayer){
            simpleView = root
            simpleViewPic = songPic
            simpleViewName = songPlayingName
            simpleViewSwitch = startPauseButton
            simpleViewNext = nextMusicButton
        }
        simpleViewSwitch.setOnClickListener {
            simpleViewSwitch.setImageResource(R.drawable.ico_music_start_black)
            if (mediaPlayer.isPlaying){
                mBinder.pause()
                simpleViewSwitch.setImageResource(R.drawable.ico_music_start_black)
            }else{
                mBinder.play()
                simpleViewSwitch.setImageResource(R.drawable.ico_music_pause)
            }
        }

        simpleViewNext.setOnClickListener {
            mBinder.playNext()
        }

        simpleView.setOnClickListener {
            BottomSheetFragment().show(supportFragmentManager,TAG)
        }
    }

    private fun serviceLogic() {
        Log.d(TAG, "执行serviceLogic方法")
        mBinder.playingSongData.observe(this@CentreActivity) {
            Log.d(TAG, "歌曲发生变化")
            simpleViewName.text = it.name
            Glide.with(this@CentreActivity)
                .load(it.al.picUrl)
                .into(simpleViewPic)
            if (mediaPlayer.isPlaying){
                simpleViewSwitch.setImageResource(R.drawable.ico_music_pause)
            }else{
                simpleViewSwitch.setImageResource(R.drawable.ico_music_start_black)
            }
            Log.d(TAG, "UI设置成功")
        }
        mBinder.getPlayingSongData()
    }

    private fun startService(){
        val serviceConnection = object :ServiceConnection{
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.d(TAG,"onServiceConnected")
                BaseApplication.mBinder = service as MusicServiceOnBind.MusicBind
                mBinder = BaseApplication.mBinder
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                return
            }
        }
        val intent = Intent(appContext, MusicServiceOnBind::class.java)
        this@CentreActivity.bindService(intent,serviceConnection, BIND_AUTO_CREATE)
    }

    private fun showBottomSheetFragment(){

    }
}