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

    private val _sortType = MutableStateFlow(SortType.LATITUDE_AND_LONGITUDE)
    private val _savedLocation = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.LATITUDE_AND_LONGITUDE ->dao.getSavedLocationByLatitudeAndLongitude()
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
            is SavedLocationEvent.SaveLocation -> {
                val latitudeAndLongitude = state.value.latitudeAndLongitude
                val description = state.value.description

                if (latitudeAndLongitude.isBlank() || description.isBlank()) {
                    return
                }
                val savedLocation = SavedLocation(
                    latitudeAndLongitude = latitudeAndLongitude,
                    description = description
                )
                viewModelScope.launch {
                    dao.upsertLocation(savedLocation)
                }
                _state.update { it.copy(
                    isAddingLocation = false,
                    latitudeAndLongitude = "",
                    description = ""
                ) }
            }
            is SavedLocationEvent.SetDescription -> {
                _state.update { it.copy(
                    description = event.description
                ) }
            }
            is SavedLocationEvent.SetLatitudeAndLongitude -> {
                _state.update { it.copy(
                    latitudeAndLongitude = event.latitudeAndLongitude
                ) }
            }
            is SavedLocationEvent.SortSavedLocations -> {
                _sortType.value = event.sortType
            }
        }
    }
}