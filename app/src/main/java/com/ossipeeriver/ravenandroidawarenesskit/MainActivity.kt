package com.ossipeeriver.ravenandroidawarenesskit

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.ossipeeriver.ravenandroidawarenesskit.databinding.ActivityMainBinding
import com.ossipeeriver.ravenandroidawarenesskit.ui.forecast.ForecastActivity
import com.ossipeeriver.ravenandroidawarenesskit.ui.location.LocationActivity
import com.ossipeeriver.ravenandroidawarenesskit.ui.location.SavedLocationListAdapter


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

        navView.setNavigationItemSelectedListener(this)
        navViewRight.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_home)
            }
            R.id.nav_forecast -> {
                val intent = Intent(this, ForecastActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_location -> {
                val locationScreen = Intent(this, LocationActivity::class.java)
                startActivity(locationScreen)
            }
        }
        return true
    }
}