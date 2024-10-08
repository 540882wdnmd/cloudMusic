package com.example.cloudmusic.utils.webs.bean.response

import android.os.Message


data class LoginResponse(
    val loginType : Int,
    val code : Int,
    val message : String,
    val token : String,
    val profile : LoginProfile,
    val cookie : String
)

data class LoginProfile(
    val nickname : String,
    val avatarUrl : String,
)