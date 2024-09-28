package com.example.cloudmusic.centre.recommend

import com.example.cloudmusic.utils.webs.ServiceCreator

import com.example.cloudmusic.utils.webs.bean.response.BannerResponse
import com.example.cloudmusic.utils.webs.service.Banner
import retrofit2.Callback

class RecommendModel {

    fun getBanner(callback : Callback<BannerResponse>){

        ServiceCreator.create<Banner>()
            .getBanner()
            .enqueue(callback)
    }
}