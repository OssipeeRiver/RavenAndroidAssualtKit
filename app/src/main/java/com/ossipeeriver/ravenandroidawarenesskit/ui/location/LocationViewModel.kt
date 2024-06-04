package com.ossipeeriver.ravenandroidawarenesskit.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ossipeeriver.ravenandroidawarenesskit.database.SavedLocation
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    val allSavedLocations: LiveData<List<SavedLocation>> = repository.allLocations.asLiveData()

    fun insert(location: SavedLocation) = viewModelScope.launch {
        repository.insert(location)
    }
}

class LocationModelFactory(private val repository: LocationRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}