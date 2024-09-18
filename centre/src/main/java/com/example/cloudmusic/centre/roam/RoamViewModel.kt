package com.example.cloudmusic.centre.roam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoamViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is roam Fragment"
    }
    val text: LiveData<String> = _text
}