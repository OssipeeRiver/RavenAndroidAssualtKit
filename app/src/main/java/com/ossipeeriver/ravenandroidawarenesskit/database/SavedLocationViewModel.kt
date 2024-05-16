package com.ossipeeriver.ravenandroidawarenesskit.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arcgismaps.mapping.view.geometryeditor.ShapeTool
import com.ossipeeriver.ravenandroidawarenesskit.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SavedLocationViewModel(
    private val dao: SavedLocationDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.LATITUDE)
    private val _savedLocation = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.LATITUDE -> dao.getSavedLocationByLatitude()
                SortType.LONGITUDE -> dao.getSavedLocationByLongitude()
                SortType.DESCRIPTION -> dao.getSavedLocationByDescription()
            }
        }

    private val _state = MutableStateFlow(SavedLocationState())

    // combines all three flows into one -> executes when any state flow receives a new value
    val state = combine(_state, _sortType, _savedLocation) {state, sortType, savedLocation ->
        state.copy(
            savedLocation = savedLocation,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SavedLocationState())

    fun onEvent(event: SavedLocationEvent) {
        when(event) {
            is SavedLocationEvent.DeleteLocation -> {
                viewModelScope.launch {
                    dao.deleteLocation(event.savedLocation)
                }
            }
            SavedLocationEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingLocation = false
                ) }
            }
            SavedLocationEvent.SaveLocation -> {
                val latitude = state.value.latitude
                val longitude = state.value.longitude
                val description = state.value.description

                if (latitude.isNaN() || longitude.isNaN() || description.isBlank()) {
                    return
                }
                val savedLocation = SavedLocation(
                    latitude = latitude,
                    longitude = longitude,
                    description = description
                )
                viewModelScope.launch {
                    dao.upsertLocation(savedLocation)
                }
                _state.update { it.copy( // TODO possible logic error
                    isAddingLocation = false,
                    latitude = 0.0,
                    longitude = 0.0,
                    description = ""
                ) }
            }
            is SavedLocationEvent.SetDescription -> {
                _state.update { it.copy(
                    description = event.Description
                ) }
            }
            is SavedLocationEvent.SetLatitude -> {
                _state.update { it.copy(
                    latitude = event.latitude
                ) }
            }
            is SavedLocationEvent.SetLongitude -> {
                _state.update { it.copy(
                    longitude = event.Longitude
                ) }
            }
            SavedLocationEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingLocation = true
                ) }
            }
            is SavedLocationEvent.SortSavedLocations -> {
                _sortType.value = event.sortType
            }
        }
    }
}