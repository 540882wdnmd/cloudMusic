package com.example.cloudmusic.utils.webs.bean.response

data class PersonalizedResponse (
    val code : Int,
    val result : List<PersonalizedResult>
)

data class PersonalizedResult(
    val id : String,
    val name : String,
    val picUrl : String
)

