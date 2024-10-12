package com.example.cloudmusic.utils.webs.bean.response

data class PlayListResponse(
    val songs : List<Song>,
    val code : Int,
    val msg : String?
)

data class Song(
    val name:String,
    val id : String,
    val ar : List<Artist>,
    var al : Album,

)

data class Artist(
    val id: String,
    val name : String,
)

data class Album(
    val id: String,
    val name: String,
    val picUrl : String
)

