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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.ossipeeriver.ravenandroidawarenesskit.R
import com.ossipeeriver.ravenandroidawarenesskit.database.SavedLocation
import com.ossipeeriver.ravenandroidawarenesskit.databinding.FragmentLocationBinding

class LocationFragment : Fragment(), LocationListener {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationManager: LocationManager

    private var currentLocation: Location? = null

    private val newSavedLocationRequestCode = 1

    private val locationViewModel: LocationViewModel by viewModels {
        LocationModelFactory((requireActivity().application as SavedLocationApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // saved location recycler view
        val recyclerView = binding.savedLocationRecyclerview
        val adapter = SavedLocationListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        locationViewModel.allSavedLocations.observe(owner = viewLifecycleOwner) {savedLocation ->
            savedLocation.let { adapter.submitList(it) }
        }

        binding.saveLocationButton.setOnClickListener {
            val intent = Intent(requireContext(), AddNewLocationActivity::class.java)
            startActivityForResult(intent, newSavedLocationRequestCode)
        }

        binding.getLocationBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            } else {
                getLocation()
            }
        }

        return root
    }

    private fun getLocation() { // TODO location should only be called on request by user
        try {
            locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 20f, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(location: Location) {
        binding.locationLatWidget.text = "${location.latitude}"
        binding.locationLongWidget.text = "${location.longitude}"
    }

    private fun onProviderDisabled() {
        Toast.makeText(requireContext(), "Please enable phone's location services for this function", Toast.LENGTH_SHORT).show()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // Required override
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

    override fun onDestroyView() {
        super.onDestroyView()
        if(::locationManager.isInitialized) {
            locationManager.removeUpdates(this)
        }
        _binding = null
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newSavedLocationRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(AddNewLocationActivity.EXTRA_REPLY)?.let { reply ->
                currentLocation?.let { location ->
                    val latLong = "${location.latitude},${location.longitude}"
                    val savedLocation = SavedLocation(
                        latitudeAndLongitude = latLong,
                        description = reply
                    )
                    locationViewModel.insert(savedLocation)
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

