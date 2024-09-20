package com.example.cloudmusic.centre.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cloudmusic.centre.databinding.FragmentLoginBinding
import com.example.cloudmusic.utils.webs.bean.response.LoginResponse

class LoginFragment : Fragment() {


    private lateinit var avatarImage : ImageView
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

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
        }

        loginButton.setOnClickListener {
            loginViewModel.loginRequest(phoneEditText.text.toString(),passwordEditText.text.toString())
        }

        return rootView
    }


    private fun observe(){
        loginViewModel.loginResponse.observe(this@LoginFragment.viewLifecycleOwner){
            if (it!=null){
                if (it.code==200){

                }
            }else{

            }
        }
    }

    private fun login(loginResponse: LoginResponse){
        if (true){

        }else{

        }
    }
}