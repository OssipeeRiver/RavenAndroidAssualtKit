package com.ossipeeriver.ravenandroidawarenesskit.ui.forecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ossipeeriver.ravenandroidawarenesskit.databinding.ActivityForcastBinding

class ForecastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForcastBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityForcastBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}