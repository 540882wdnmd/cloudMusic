package com.example.cloudmusic.centre.dynamic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DynamicViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dynamic Fragment"
    }
    val text: LiveData<String> = _text
}