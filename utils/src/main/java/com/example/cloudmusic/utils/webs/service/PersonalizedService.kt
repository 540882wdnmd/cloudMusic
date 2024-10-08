package com.example.cloudmusic.utils.webs.service

import retrofit2.http.GET
import com.example.cloudmusic.utils.webs.WebConstant.Personalized.API_PERSONALIZED
import com.example.cloudmusic.utils.webs.bean.response.PersonalizedResponse
import retrofit2.Call

interface PersonalizedService {

    @GET(API_PERSONALIZED)
    fun getPersonalized() : Call<PersonalizedResponse>
}