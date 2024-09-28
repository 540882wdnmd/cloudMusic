package com.example.cloudmusic.centre.login

import androidx.datastore.preferences.core.edit
import com.example.cloudmusic.utils.datastore.dataStoreInstance
import com.example.cloudmusic.utils.datastore.getBooleanData
import com.example.cloudmusic.utils.datastore.preferenceAvatar
import com.example.cloudmusic.utils.datastore.preferenceCookie
import com.example.cloudmusic.utils.datastore.preferenceNickname
import com.example.cloudmusic.utils.datastore.preferencePassword
import com.example.cloudmusic.utils.datastore.preferencePhone
import com.example.cloudmusic.utils.datastore.preferenceStatus
import com.example.cloudmusic.utils.datastore.putBooleanData
import com.example.cloudmusic.utils.datastore.putStringData
import com.example.cloudmusic.utils.webs.ServiceCreator
import com.example.cloudmusic.utils.webs.bean.data.Account
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

    /**
     * 保存用户信息
     */
    suspend fun saveAccountInfo(account: Account){
        putStringData(preferenceNickname,account.nickname)
        putStringData(preferenceAvatar,account.avatarUrl)
        putBooleanData(preferenceStatus,account.loginStatus)
        putStringData(preferenceCookie,account.cookie)
    }

    /**
     * 获取登录状态
     */
    suspend fun getLoginStatus() = getBooleanData(preferenceStatus)

    /**
     * 登出请求
     */
    fun logoutRequest(callback: Callback<LogoutResponse>){
        ServiceCreator.create<LogoutService>()
            .getLogoutResponse()
            .enqueue(callback)
    }

    /**
     * 登出清除用户信息
     */
    suspend fun clearAccountInfo(){
        putStringData(preferencePhone,"")
        putStringData(preferencePassword,"")
        putStringData(preferenceNickname,"")
        putStringData(preferenceAvatar,"")
        putBooleanData(preferenceStatus,false)
        putStringData(preferenceCookie,"")
    }
}