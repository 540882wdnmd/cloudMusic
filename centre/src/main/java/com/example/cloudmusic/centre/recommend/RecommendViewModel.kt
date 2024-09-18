package com.example.cloudmusic.centre.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecommendViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is recommend Fragment"
    }
    val text: LiveData<String> = _text
}