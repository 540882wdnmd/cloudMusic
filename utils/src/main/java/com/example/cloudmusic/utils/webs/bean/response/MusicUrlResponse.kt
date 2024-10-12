package com.example.cloudmusic.utils.webs.bean.response

data class MusicUrlResponse(
    val code : Int,
    val data : List<Data>,
    val msg : String?
)

data class Data(
    val url : String,
)
