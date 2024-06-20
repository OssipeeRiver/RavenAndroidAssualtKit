package com.ossipeeriver.ravenandroidawarenesskit.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ForecastViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Forecast Data"
    }
    val text: LiveData<String> = _text
}