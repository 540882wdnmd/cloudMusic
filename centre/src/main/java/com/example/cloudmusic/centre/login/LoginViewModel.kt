package com.example.cloudmusic.centre.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudmusic.utils.convertErrorBody
import com.example.cloudmusic.utils.webs.bean.data.Account
import com.example.cloudmusic.utils.webs.bean.response.LoginResponse
import com.example.cloudmusic.utils.webs.bean.response.LogoutResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val loginModel by lazy { LoginModel() }

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse : LiveData<LoginResponse?>
            get() = _loginResponse

    private val _logoutResponse = MutableLiveData<LogoutResponse?>()
    val logoutResponse : LiveData<LogoutResponse?>
        get() = _logoutResponse


    fun loginRequest(phone : String,password : String){
        loginModel.loginRequest(phone,password,object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _loginResponse.value = if (response.isSuccessful){
                    viewModelScope.launch(Dispatchers.Default){
                        loginModel.saveUserInfo(phone,password)
                    }
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

    fun logoutRequest(){
        loginModel.logoutRequest(object : Callback<LogoutResponse>{
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>
            ) {
                _logoutResponse.value = if (response.isSuccessful){
                    response.body()
                }else{
                    convertErrorBody<LogoutResponse>(response.errorBody())
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                t.printStackTrace()
                _logoutResponse.value = null
            }

        })
    }


    fun getLoginStatus(callback : LoginFragment.Callback) {
        viewModelScope.launch(Dispatchers.Main){
            callback.getLoginStatus(loginModel.getLoginStatus())
        }
    }

    fun saveAccountInfo(account: Account){
        viewModelScope.launch(Dispatchers.IO){
            loginModel.saveAccountInfo(account)
        }
    }

    fun clearAccountInfo(){
        viewModelScope.launch(Dispatchers.IO){
            loginModel.clearAccountInfo()
        }
    }



}