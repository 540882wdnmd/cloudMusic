package com.example.cloudmusic.centre.login

import com.example.cloudmusic.utils.webs.ServiceCreator
import com.example.cloudmusic.utils.webs.bean.data.User
import retrofit2.Callback
import com.example.cloudmusic.utils.webs.bean.response.LoginResponse
import com.example.cloudmusic.utils.webs.service.LoginService

class LoginModel {

    fun loginRequest(phone : String,password : String,callback : Callback<LoginResponse>){

        ServiceCreator.create<LoginService>()
            .getLoginResponse(User(phone, password))
            .enqueue(callback)
    }
}