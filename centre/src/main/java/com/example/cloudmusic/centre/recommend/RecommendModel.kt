package com.example.cloudmusic.centre.recommend

import com.example.cloudmusic.utils.webs.ServiceCreator

import com.example.cloudmusic.utils.webs.bean.response.BannerResponse
import com.example.cloudmusic.utils.webs.bean.response.PersonalizedResponse
import com.example.cloudmusic.utils.webs.service.BannerService
import com.example.cloudmusic.utils.webs.service.PersonalizedService
import retrofit2.Callback

class RecommendModel {

    fun getBanner(callback : Callback<BannerResponse>){

        ServiceCreator.create<BannerService>()
            .getBanner()
            .enqueue(callback)
    }

    fun getPersonalized(callback: Callback<PersonalizedResponse>){

        ServiceCreator.create<PersonalizedService>()
            .getPersonalized()
            .enqueue(callback)
    }


}