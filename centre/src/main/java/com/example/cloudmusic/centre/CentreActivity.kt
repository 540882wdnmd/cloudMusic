package com.example.cloudmusic.centre

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.cloudmusic.centre.databinding.ActivityCentreBinding
import com.example.cloudmusic.utils.TAG
import com.example.cloudmusic.utils.base.BaseApplication
import com.example.cloudmusic.utils.base.BaseApplication.Companion
import com.example.cloudmusic.utils.base.BaseApplication.Companion.appContext
import com.example.cloudmusic.utils.hideKeyboard
import com.example.cloudmusic.utils.service.MusicServiceOnBind
import com.example.cloudmusic.utils.webs.bean.response.Song
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference

class CentreActivity : AppCompatActivity() {

    private lateinit var simpleViewPic : ImageView
    private lateinit var simpleViewName : TextView
    private lateinit var simpleViewSwitch : ImageButton
    private lateinit var simpleViewNext : ImageButton
    private lateinit var binding : ActivityCentreBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbarMain: Toolbar
    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var navController : NavController

    private lateinit var mBinder : MusicServiceOnBind.MusicBind


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.activity = WeakReference(this)

        binding  = ActivityCentreBinding.inflate(layoutInflater)
        setContentView(binding.root)//绑定布局

        setDrawerLayout()
        setBottomNav()
        setSimpleView()

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
        val intent = Intent(appContext,MusicServiceOnBind::class.java)
        this@CentreActivity.bindService(intent,serviceConnection, BIND_AUTO_CREATE)

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"执行onStart")
    }
    override fun onResume() {
        super.onResume()
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
            simpleViewPic = songPic
            simpleViewName = songPlayingName
            simpleViewSwitch = startPauseButton
            simpleViewNext = nextMusicButton
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
            Log.d(TAG, "UI设置成功")
        }
        mBinder.getPlayingSongData()
    }
}