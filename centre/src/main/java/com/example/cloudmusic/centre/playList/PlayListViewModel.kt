package com.example.cloudmusic.centre.playList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloudmusic.utils.convertErrorBody
import com.example.cloudmusic.utils.webs.bean.response.MusicUrlResponse
import com.example.cloudmusic.utils.webs.bean.response.PlayListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayListViewModel : ViewModel() {

    private val playListModel by lazy { PlayListModel() }
    private val _playList = MutableLiveData<PlayListResponse?>()
    val playList : LiveData<PlayListResponse?>
        get() = _playList

    private val _musicUrl  = MutableLiveData<MusicUrlResponse?>()
    val musicUrl : LiveData<MusicUrlResponse?>
        get() = _musicUrl

    fun getPlayList(id : String){
        playListModel.getPlayList(id,object : Callback<PlayListResponse>{
            override fun onResponse(
                call: Call<PlayListResponse>,
                response: Response<PlayListResponse>
            ) {
                _playList.value = if (response.isSuccessful){
                    response.body()
                }else{
                    convertErrorBody<PlayListResponse>(response.errorBody())
                }
            }

            override fun onFailure(call: Call<PlayListResponse>, t: Throwable) {
                t.printStackTrace()
                _playList.value = null
            }

        })
    }

    fun getMusicUrl(idAll : String){
        playListModel.getMusicUrl(idAll,object : Callback<MusicUrlResponse>{
            override fun onResponse(
                call: Call<MusicUrlResponse>,
                response: Response<MusicUrlResponse>
            ) {
                _musicUrl.value = if (response.isSuccessful){
                    response.body()
                }else{
                    convertErrorBody<MusicUrlResponse>(response.errorBody())
                }
            }

            override fun onFailure(call: Call<MusicUrlResponse>, t: Throwable) {
                t.printStackTrace()
                _musicUrl.value = null
            }
        })
    }
}