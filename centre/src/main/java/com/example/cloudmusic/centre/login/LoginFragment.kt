package com.example.cloudmusic.centre.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.example.cloudmusic.centre.databinding.FragmentLoginBinding
import com.example.cloudmusic.utils.toast
import com.example.cloudmusic.utils.webs.bean.response.LoginResponse
import com.google.android.material.textfield.TextInputLayout

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

    private var _binding : FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

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

        loginButton.setOnClickListener {
            loginViewModel.loginRequest(phoneEditText.text.toString(),passwordEditText.text.toString())
        }

        observe()

        return rootView
    }


    private fun observe(){
        loginViewModel.loginResponse.observe(this@LoginFragment.viewLifecycleOwner){
            if (it!=null){
                if (it.code==200){
                    login(it)
                }else if (it.code ==400){
                    this.toast("登录失败")
                }
            }else{
                this.toast("网络连接错误")
            }
        }
    }

    private fun login(loginResponse: LoginResponse){
        welcomeTextView.visibility = View.VISIBLE
        nicknameTextView.visibility = View.VISIBLE
        nicknameTextView.text = loginResponse.profile.nickname

        phoneInputLayout.visibility = View.GONE
        passwordInputLayout.visibility = View.GONE

        Glide.with(this)
            .load(loginResponse.profile.avatarUrl)
            .centerCrop()
            .into(avatarImage)

        loginButton.text = "退出登录"
    }
}