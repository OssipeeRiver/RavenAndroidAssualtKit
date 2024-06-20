package com.ossipeeriver.ravenandroidawarenesskit.ui.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ossipeeriver.ravenandroidawarenesskit.R
import com.ossipeeriver.ravenandroidawarenesskit.location_db.SavedLocation
import com.ossipeeriver.ravenandroidawarenesskit.databinding.ActivityLocationBinding

@Suppress("DEPRECATION")
class LocationActivity : AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityLocationBinding
    private lateinit var locationManager: LocationManager
    private var currentLocation: Location? = null
    private val newSavedLocationRequestCode = 1

    private val locationViewModel: LocationViewModel by viewModels {
        LocationModelFactory((application as SavedLocationApplication).repository)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                onProviderDisabled()
            }
        }
    }
    companion object { private const val REQUEST_LOCATION_PERMISSION = 1 }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedLocationSearchView = findViewById<SearchView>(R.id.saved_location_search_view)

        // recycler view
        val recyclerView = binding.savedLocationRecyclerview
        val adapter = SavedLocationListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // observe ViewModel
        locationViewModel.allSavedLocations.observe(this) {savedLocation ->
            savedLocation.let { adapter.submitList(it) }
            Log.d("LOCATION ACTIVITY", "running: locationViewModel.allSavedLocations.observe")
        }

        binding.saveLocationButton.setOnClickListener {
            Log.d("Save Location BTN", "Pressed save location button")
            val intent = Intent(this, AddNewLocationActivity::class.java)
            startActivityForResult(intent, newSavedLocationRequestCode)
        }

        binding.getLocationBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            } else {
                getLocation()
            }
        }

        savedLocationSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun getLocation() { // TODO location should only be called on request by user
        try {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 20f, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(location: Location) {
        binding.locationLatWidget.text = "${location.latitude}"
        binding.locationLongWidget.text = "${location.longitude}"

        currentLocation = location
    }

    private fun onProviderDisabled() {
        Toast.makeText(this, "Please enable phone's location services for this function", Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("LOCATION ACTIVITY", "onActivityResult called with requestCode: $requestCode and resultCode: $resultCode")

        if (requestCode == newSavedLocationRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(AddNewLocationActivity.EXTRA_REPLY)?.let { description ->
                val latitude = currentLocation?.latitude
                val longitude = currentLocation?.longitude

                val gridToSave = "$latitude , $longitude"
                val savedLocation = SavedLocation(
                    latitudeAndLongitude = gridToSave,
                    description = description
                )
                locationViewModel.insert(savedLocation)
            }
        } else {
            Toast.makeText(this,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::locationManager.isInitialized) {
            locationManager.removeUpdates(this)
        }
    }
}

