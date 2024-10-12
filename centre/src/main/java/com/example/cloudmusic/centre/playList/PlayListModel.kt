package com.example.cloudmusic.centre.playList

import com.example.cloudmusic.utils.webs.ServiceCreator
import com.example.cloudmusic.utils.webs.bean.response.MusicUrlResponse
import com.example.cloudmusic.utils.webs.bean.response.PlayListResponse
import com.example.cloudmusic.utils.webs.service.MusicUrlService
import com.example.cloudmusic.utils.webs.service.PlayListService
import retrofit2.Callback

class PlayListModel {

    fun getPlayList(id : String,callback: Callback<PlayListResponse>){

        ServiceCreator.create<PlayListService>()
            .getPlayList(id)
            .enqueue(callback)
    }

    fun getMusicUrl(idAll: String,callback : Callback<MusicUrlResponse>){

        ServiceCreator.create<MusicUrlService>()
            .getMusicUrl(idAll)
            .enqueue(callback)
    }
}