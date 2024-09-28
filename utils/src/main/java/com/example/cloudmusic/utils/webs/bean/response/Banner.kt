package com.example.cloudmusic.utils.webs.bean.response

data class BannerResponse(
    val code : Int,
    val banners : List<Banner>?,
)

data class Banner(
    val bannerId : String,
    val pic : String,
    val url : String
)