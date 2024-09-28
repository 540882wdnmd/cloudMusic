package com.example.cloudmusic.utils.webs.bean.response

data class MusicUrl(
    val code : Int,
    val data : Data,
)

data class Data(
    val url : String,
)
