package com.example.cloudmusic.centre.login

import androidx.datastore.preferences.core.edit
import com.example.cloudmusic.utils.datastore.dataStoreInstance
import com.example.cloudmusic.utils.datastore.getBooleanData
import com.example.cloudmusic.utils.datastore.preferenceNickname
import com.example.cloudmusic.utils.datastore.preferencePassword
import com.example.cloudmusic.utils.datastore.preferenceStatus
import com.example.cloudmusic.utils.datastore.putStringData
import com.example.cloudmusic.utils.webs.ServiceCreator
import com.example.cloudmusic.utils.webs.bean.data.User
import retrofit2.Callback
import com.example.cloudmusic.utils.webs.bean.response.LoginResponse
import com.example.cloudmusic.utils.webs.bean.response.LogoutResponse
import com.example.cloudmusic.utils.webs.service.LoginService
import com.example.cloudmusic.utils.webs.service.LogoutService


class LoginModel {

    fun loginRequest(phone : String,password : String,callback : Callback<LoginResponse>){

        ServiceCreator.create<LoginService>()
            .getLoginResponse(phone,password)
            .enqueue(callback)
    }

    /**
     * 保存用户名和密码到 DataStore
     */
    suspend fun saveUserInfo(phone: String, password: String) {
        putStringData(preferenceNickname, phone)
        putStringData(preferencePassword, password)
    }

    suspend fun getLoginStatus() = getBooleanData(preferenceStatus)

    fun logoutRequest(callback: Callback<LogoutResponse>){
        ServiceCreator.create<LogoutService>()
            .getLogoutResponse()
            .enqueue(callback)
    }
}