package com.example.cloudmusic.utils.webs.service

import com.example.cloudmusic.utils.webs.WebConstant.Login.API_LOGIN
import com.example.cloudmusic.utils.webs.bean.data.User
import com.example.cloudmusic.utils.webs.bean.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST(API_LOGIN)
    fun getLoginResponse(@Body user: User): Call<LoginResponse>

}