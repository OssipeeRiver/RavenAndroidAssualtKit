package com.ossipeeriver.ravenandroidawarenesskit.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.mapping.BasemapStyle
import com.arcgismaps.mapping.view.MapView

import com.ossipeeriver.ravenandroidawarenesskit.databinding.FragmentHomeBinding

class MapFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView = binding.mapView

        lifecycle.addObserver(mapView)
        setApiKey()
        setupMap(mapView)
    }
    private fun setApiKey() {

        ArcGISEnvironment.apiKey = ApiKey.create("AAPK007ac4272f2246018626a29e68e20e74RLlbyMb7weVEdL9kNRy4D_sz9pdyeUrXeaduxO2EjC1sKPNVJOAO7nSyBUsFofiv")

    }
    private fun setupMap(mapView: MapView) {
        val map = ArcGISMap(BasemapStyle.ArcGISTopographic)
        mapView.map = map

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


