package com.example.cloudmusic.utils.webs.service

import com.example.cloudmusic.utils.webs.WebConstant.MusicUrl.API_MUSIC_URL
import com.example.cloudmusic.utils.webs.bean.response.MusicUrl
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface MusicUrlService {
    @GET(API_MUSIC_URL)
    fun getMusicUrl(@Body id : String) : Call<MusicUrl>
}