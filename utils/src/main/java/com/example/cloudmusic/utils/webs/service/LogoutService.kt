package com.example.cloudmusic.utils.webs.service

import com.example.cloudmusic.utils.webs.WebConstant.Logout.API_LOGOUT
import com.example.cloudmusic.utils.webs.bean.response.LogoutResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface LogoutService {

    @GET(API_LOGOUT)
    fun getLogoutResponse() : Call<LogoutResponse>
}