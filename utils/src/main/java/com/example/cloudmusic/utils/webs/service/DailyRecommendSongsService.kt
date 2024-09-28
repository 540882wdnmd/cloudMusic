package com.example.cloudmusic.utils.webs.service

import com.example.cloudmusic.utils.webs.WebConstant.DailyRecommendSongs.API_DAILY_RECOMMEND_SONGS
import com.example.cloudmusic.utils.webs.bean.response.DailyRecommendSongsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyRecommendSongsService {

    @GET(API_DAILY_RECOMMEND_SONGS)
    fun getDailyRecommendSongs(@Query("cookie") cookie : String) : Call<DailyRecommendSongsResponse>
}