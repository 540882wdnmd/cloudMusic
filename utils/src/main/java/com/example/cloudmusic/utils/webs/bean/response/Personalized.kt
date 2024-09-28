package com.example.cloudmusic.utils.webs.bean.response

data class Personalized (
    val code : Int,
    val result : List<Result>
)

data class Result(
    val id : String,
    val name : String,
    val picUrl : String
)

