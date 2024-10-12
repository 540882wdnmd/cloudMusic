package com.example.cloudmusic.utils.webs.service

import retrofit2.http.GET
import com.example.cloudmusic.utils.webs.WebConstant.PlayList.API_PLAY_LIST
import com.example.cloudmusic.utils.webs.bean.response.PlayListResponse
import retrofit2.Call
import retrofit2.http.Query

interface PlayListService {

    @GET(API_PLAY_LIST)
    fun getPlayList(@Query("id") id : String) : Call<PlayListResponse>

    @GET(API_PLAY_LIST)
    fun getPlayListLimit(@Query("id") id : String,
                         @Query("limit") limit : Int,
                         @Query("offset") offset : Int) : Call<PlayListResponse>
}