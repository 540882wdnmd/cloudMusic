package com.example.cloudmusic.utils.webs.bean.data

data class Account(
    val avatarUrl : String,
    val nickname : String,
    val loginStatus : Boolean = false,
    val cookie : String
)
