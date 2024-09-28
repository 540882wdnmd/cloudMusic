package com.example.cloudmusic.utils.webs.service


import retrofit2.http.GET
import com.example.cloudmusic.utils.webs.WebConstant.Banner.API_BANNER
import com.example.cloudmusic.utils.webs.bean.response.BannerResponse
import retrofit2.Call
import retrofit2.http.Query

interface Banner {

    @GET(API_BANNER)
    fun getBanner(@Query("type") int: Int = 1) : Call<BannerResponse>
}