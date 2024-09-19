package com.example.cloudmusic.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.ResponseBody

const val SECOND_TIME = 1000L
const val MINUTE_TIME = 60 * SECOND_TIME
const val HOUR_TIME = 60 * MINUTE_TIME
const val DAY_TIME = 24 * HOUR_TIME

/** 将简单类名作为TAG（不包含包名），用于日志打印
 * 定义TAG拓展属性*/
val Activity.TAG
    get() = this::class.simpleName!!

val Fragment.TAG
    get() = this::class.simpleName!!

val ViewModel.TAG
    get() = this::class.simpleName!!

inline fun <reified T> convertErrorBody(responseErrorBody : ResponseBody?) : T?{
    runCatching {
        return Gson().fromJson(responseErrorBody!!.string(), T::class.java)
    }
    //异常不做处理
    return null
}

