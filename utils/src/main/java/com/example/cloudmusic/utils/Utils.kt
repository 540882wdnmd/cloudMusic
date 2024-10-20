package com.example.cloudmusic.utils

import android.app.Activity
import android.app.Service
import android.content.Context
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.cloudmusic.utils.webs.bean.response.Artist
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody

/**
 * 单位ms 定义时间
 */
const val SECOND_TIME = 1000
const val MINUTE_TIME = 60 * SECOND_TIME
const val HOUR_TIME = 60 * MINUTE_TIME
const val DAY_TIME = 24 * HOUR_TIME

/**
 * MediaPlayer转换时间格式
 */
fun convertTimeFormat(duration : Int) : String{
    val minuteTime = duration/ MINUTE_TIME
    val secondTime = (duration % MINUTE_TIME) / SECOND_TIME
    val stringBuilder = StringBuilder()
    return stringBuilder.append(minuteTime).append(":").append(secondTime).toString()
}

/**
 * 定义状态
 */
const val OK = 200
const val ERROR = 400

/**
 * Intent 的setAction()参数
 */
const val START_ACTIVITY = "start activity"
const val START_SERVICE = "start service"
const val SEND_BROADCAST = "send broadcast"

/**
 * Intent 的Key 值
 */
const val PERSONALIZED_ID = "personalized ID"

/** 将简单类名作为TAG（不包含包名），用于日志打印
 * 定义TAG拓展属性*/
val Activity.TAG
    get() = this::class.simpleName!!

val Fragment.TAG
    get() = this::class.simpleName!!

val ViewModel.TAG
    get() = this::class.simpleName!!

val Service.TAG
    get() = this::class.simpleName!!

/**
 * 拓展方法 弹出toast
 */
private var toast : Toast? = null
fun Context.toast(message : String,showTime : Int = Toast.LENGTH_SHORT) {
    toast?.cancel() //取消上一个Toast 避免延迟显示 增加用户体验
    toast = Toast.makeText(this,message,showTime)
    toast!!.show()
}
fun Fragment.toast(message: String,showTime: Int = Toast.LENGTH_SHORT) = requireContext().toast(message,showTime)


/**
 * 网络请求失败 将获取回来的ErrorResponseBody转换成类
 */
inline fun <reified T> convertErrorBody(responseErrorBody : ResponseBody?) : T?{
    runCatching {
        return Gson().fromJson(responseErrorBody!!.string(), T::class.java)
    }
    return null
}

/**
 * 拦截其他View的触摸事件 实现点击任何地方收起软键盘
 * @param ev 触摸事件
 * @param excludeViews 免疫该方法的View
 */

fun Activity.hideKeyboard(ev : MotionEvent, excludeViews : List<View>?) {
    if (ev.action == MotionEvent.ACTION_DOWN){
        if (excludeViews != null) {
            if (excludeViews.isNotEmpty()){
                for(view in excludeViews){
                    if (isTouchView(view , ev)){
                        return
                    }
                }
            }
        }
        val view = this.currentFocus
        if (view?.let { isShouldHideInput(it,ev) } == true){
            val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken,0)
        }
    }
}

/*
用于判断给定的触摸事件（MotionEvent）是否发生在特定的视图（View）上
 */
fun isTouchView(view : View,event : MotionEvent) : Boolean {
    val  leftTop = intArrayOf(0,0) //屏幕左上角的坐标
    view.getLocationInWindow(leftTop); //定位view
    val left = leftTop[0]; //左边界
    val top = leftTop[1]; //上边界
    val bottom = top + view.height; //下边界
    val right = left + view.width; //右边界
    return (event.rawX > left && event.rawX < right
            && event.rawY > top && event.rawY < bottom); //触摸事件的绝对坐标是否在试图内

}

/*
判断是否需要收起软键盘
 */
fun isShouldHideInput(view: View,event:MotionEvent):Boolean{
    if (view is EditText) {
        return !isTouchView(view, event);
    }
    return false;
}

/**
 * 获取Cookie
 */


/**
 * 用'/'隔开字符
 */

fun songArtistString(arList : List<Artist>) : String{
    val builder : StringBuilder = StringBuilder()
    for (ar in arList){
        builder.append(ar.name).append("/")
    }
    builder.deleteCharAt(builder.length-1)
    return builder.toString()
}

