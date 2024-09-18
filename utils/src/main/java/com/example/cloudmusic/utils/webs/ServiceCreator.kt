package com.example.cloudmusic.utils.webs

import com.example.cloudmusic.utils.webs.WebConstant.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {

    private const val TIME_OUT = 30.toLong()

    private val client = OkHttpClient.Builder()
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client) //通过 client 将超时设置为 30s 以防止服务器响应较慢获取不了数据的情况
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * 使用 ServiceCreator.create(XXXService::class.java) 返回一个 Service 代理对象
     */
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /**
     * 使用 ServiceCreator.create<XXXService> 返回一个 Service 代理对象
     */
    inline fun <reified T> create(): T = create(T::class.java)


}