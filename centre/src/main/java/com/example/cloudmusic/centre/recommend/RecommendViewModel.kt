package com.example.cloudmusic.centre.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloudmusic.utils.convertErrorBody
import com.example.cloudmusic.utils.webs.bean.response.Banner
import com.example.cloudmusic.utils.webs.bean.response.BannerResponse
import com.example.cloudmusic.utils.webs.bean.response.PersonalizedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendViewModel : ViewModel() {

    private val recommendModel by lazy { RecommendModel() }
    private val _banner = MutableLiveData<BannerResponse?>()
    val banner : LiveData<BannerResponse?>
        get() = _banner

    private val _personalized = MutableLiveData<PersonalizedResponse?>()
    val personalized : LiveData<PersonalizedResponse?>
        get() = _personalized

    fun getBanner(){
        recommendModel.getBanner(object : Callback<BannerResponse>{
            override fun onResponse(
                call: Call<BannerResponse>,
                response: Response<BannerResponse>
            ) {
                _banner.value=if (response.isSuccessful){
                    response.body()
                }else{
                    convertErrorBody<BannerResponse>(response.errorBody())
                }
            }

            override fun onFailure(call: Call<BannerResponse>, t: Throwable) {
                t.printStackTrace()
                _banner.value=null
            }

        })
    }

    fun getPersonalized(){
        recommendModel.getPersonalized(object : Callback<PersonalizedResponse>{
            override fun onResponse(
                call: Call<PersonalizedResponse>,
                response: Response<PersonalizedResponse>
            ) {
                _personalized.value=if (response.isSuccessful){
                    response.body()
                }else{
                    convertErrorBody<PersonalizedResponse>(response.errorBody())
                }
            }

            override fun onFailure(call: Call<PersonalizedResponse>, t: Throwable) {
                t.printStackTrace()
                _personalized.value=null
            }

        })
    }

}