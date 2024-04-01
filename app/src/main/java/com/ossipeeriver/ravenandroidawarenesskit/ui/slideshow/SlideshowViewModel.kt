package com.ossipeeriver.ravenandroidawarenesskit.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Forecast Data"
    }
    val text: LiveData<String> = _text
}