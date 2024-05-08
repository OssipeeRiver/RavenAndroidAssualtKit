package com.ossipeeriver.ravenandroidawarenesskit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import com.arcgismaps.location.LocationDisplayAutoPanMode
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.mapping.BasemapStyle
import com.arcgismaps.mapping.view.LocationDisplay
import com.arcgismaps.mapping.view.MapView
import com.ossipeeriver.ravenandroidawarenesskit.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView

    private lateinit var locationDisplay: LocationDisplay
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setApiKey()

        mapView = binding.mapView
        locationDisplay = mapView.locationDisplay

        lifecycle.addObserver(mapView)
        setupMap()
    }

    private fun setApiKey() {
        ArcGISEnvironment.apiKey = ApiKey.create("AAPK099f686feb6a4e3289a831e647ab6172AUUtTyVSFmH6TbAOU2LJzsaQQnPymytjcF5Kd7vc-IiJI9RkCe4b0NAa9ij1GExJ") // TODO secure this API key
    }

    private fun setupMap() {
        val map = ArcGISMap(BasemapStyle.ArcGISTopographic)
        mapView.map = map

        ArcGISEnvironment.applicationContext = requireContext()

        locationDisplay.setAutoPanMode(LocationDisplayAutoPanMode.Recenter)

        lifecycleScope.launch {
            // start the map view's location display
            locationDisplay.dataSource.start()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}