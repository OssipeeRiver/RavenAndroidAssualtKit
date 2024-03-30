package com.ossipeeriver.ravenandroidawarenesskit.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Location Fragment"
    }
    val text: LiveData<String> = _text
}