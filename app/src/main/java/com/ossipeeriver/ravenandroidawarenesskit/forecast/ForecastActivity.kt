package com.ossipeeriver.ravenandroidawarenesskit.ui.forecast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ossipeeriver.ravenandroidawarenesskit.databinding.ActivityForecastBinding

class ForecastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}