package com.ossipeeriver.ravenandroidawarenesskit

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.ossipeeriver.ravenandroidawarenesskit.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: NavigationView = binding.navViewLeft
        val navViewRight: NavigationView = binding.navViewRight

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        navView.setupWithNavController(navController)
        navViewRight.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return super.onSupportNavigateUp()
    }
}