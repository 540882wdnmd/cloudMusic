package com.example.cloudmusic.centre.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cloudmusic.utils.convertErrorBody
import com.example.cloudmusic.utils.webs.bean.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val loginModel by lazy { LoginModel() }
    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse : LiveData<LoginResponse?>
            get() = _loginResponse


    fun loginRequest(phone : String,password : String){
        loginModel.loginRequest(phone,password,object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _loginResponse.value = if (response.isSuccessful){
                    response.body()
                }else{
                    convertErrorBody<LoginResponse>(response.errorBody())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace() // 打印在控制台
                _loginResponse.value = null
            }
        })
    }

}