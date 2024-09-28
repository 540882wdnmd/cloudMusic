package com.example.cloudmusic.centre.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloudmusic.utils.webs.bean.data.BannerImage

class RecommendViewModel : ViewModel() {

    private val _bannerImage = MutableLiveData<List<BannerImage>>()
    val bannerImage : LiveData<List<BannerImage>>
        get() = _bannerImage


}