package com.example.cloudmusic.utils.webs

/**
 * 封装网络响应状态类
 */
sealed class HttpResponse<out T>{ //协变

    data class Success<out T>(val response: T) : HttpResponse<T>()
    data class Error(val errMsg: String) : HttpResponse<Nothing>()
    data class ErrorBody<out T>(val response: T) : HttpResponse<T>()

    /**
     * object是单例类
     */
    data object Loading: HttpResponse<Nothing>() //loading只是一个状态，不用存储数据

}