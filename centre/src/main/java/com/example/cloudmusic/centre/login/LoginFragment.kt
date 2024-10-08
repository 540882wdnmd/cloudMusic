package com.example.cloudmusic.centre.login

import android.os.Bundle
import android.preference.PreferenceDataStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.example.cloudmusic.centre.R
import com.example.cloudmusic.centre.databinding.FragmentLoginBinding
import com.example.cloudmusic.utils.TAG
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
import com.example.cloudmusic.utils.toast
import com.example.cloudmusic.utils.webs.bean.data.Account
import com.example.cloudmusic.utils.webs.bean.response.LoginResponse
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private lateinit var avatarImage : ImageView
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var welcomeTextView: TextView
    private lateinit var nicknameTextView: TextView
    private lateinit var phoneInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout

    private val loginViewModel by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }

    private var loginStatus = false
    private var _binding : FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    interface Callback{
        fun getLoginStatus(callLoginStatus : Boolean)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val rootView = binding.root
        with(binding){
            avatarImage = usersAvatarImage
            phoneEditText = editTextUserPhone
            passwordEditText = editTextUserPassword
            loginButton = btnLogin
            welcomeTextView = textWelcomeLoginFragment
            nicknameTextView = textNicknameLoginFragment
            phoneInputLayout = inputLayoutUserPhone
            passwordInputLayout = inputLayoutUserPassword
        }
        observe()
        init()
        loginButton.setOnClickListener {
            getLoginStatus()
            Log.d(TAG,loginStatus.toString())
            if (!loginStatus){
                loginViewModel.loginRequest(phoneEditText.text.toString(), passwordEditText.text.toString())
            }else{
                loginViewModel.logoutRequest()
            }
        }

        return rootView
    }

    private fun observe(){
        loginViewModel.loginResponse.observe(viewLifecycleOwner){
            if (it!=null){
                if (it.code==200){
                    login(it)
                }else if (it.code ==400){
                    toast(it.message)
                }
            }else{
                toast("网络连接错误")
            }
        }

        loginViewModel.logoutResponse.observe(viewLifecycleOwner){
            if (it!=null){
                if (it.code==200){
                    logout()
                }else{
                    toast("登出失败")
                }
            }else{
                toast("网络连接错误")
            }
        }

    }

    private fun init(){
        getLoginStatus()
        if (loginStatus){
            lifecycleScope.launch(Dispatchers.Main) {
                dataStoreInstance.edit {
                    it[preferenceNickname]?.let { it1 -> it[preferenceAvatar]?.let { it2 ->
                        loginUI(it1,
                            it2
                        )
                    } }
                }
            }
        }else{
            logout()
        }
    }

    private fun login(loginResponse: LoginResponse){
        loginViewModel.saveAccountInfo(Account(
            loginResponse.profile.avatarUrl,
            loginResponse.profile.nickname,
            true,
            loginResponse.cookie,
        ))
        loginUI(loginResponse.profile.nickname,loginResponse.profile.avatarUrl)
    }

    private fun logout(){
        loginViewModel.clearAccountInfo()
        logoutUI()
    }

    private fun loginUI(nickname:String,avatarUrl: String){
        welcomeTextView.visibility = View.VISIBLE
        nicknameTextView.visibility = View.VISIBLE
        nicknameTextView.visibility = View.VISIBLE
        loginButton.text = "退出登录"

        nicknameTextView.text = nickname

        phoneInputLayout.visibility = View.GONE
        passwordInputLayout.visibility = View.GONE

        Glide.with(this)
            .load(avatarUrl)
            .centerCrop()
            .into(avatarImage)
    }

    private fun logoutUI(){
        welcomeTextView.visibility = View.GONE
        nicknameTextView.visibility = View.GONE
        nicknameTextView.visibility = View.GONE

        phoneEditText.text = null
        passwordEditText.text = null
        phoneInputLayout.visibility = View.VISIBLE
        passwordInputLayout.visibility = View.VISIBLE

        avatarImage.setImageResource(R.drawable.ico_mine_black)

        loginButton.text = "登录"
    }

    private fun getLoginStatus(){
        loginViewModel.getLoginStatus(object : Callback{
            override fun getLoginStatus(callLoginStatus: Boolean) {
                loginStatus = callLoginStatus
            }
        })
    }


}