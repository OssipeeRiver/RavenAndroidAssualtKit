package com.ossipeeriver.ravenandroidawarenesskit.database

import com.ossipeeriver.ravenandroidawarenesskit.SortType

sealed interface SavedLocationEvent {
    object SaveLocation: SavedLocationEvent
    data class SetLatitude(val latitude: Double): SavedLocationEvent
    data class SetLongitude(val Longitude: Double): SavedLocationEvent
    data class SetDescription(val Description: String): SavedLocationEvent
    object ShowDialog: SavedLocationEvent
    object HideDialog: SavedLocationEvent
    data class SortSavedLocations(val sortType: SortType): SavedLocationEvent
    data class DeleteLocation(val savedLocation: SavedLocation): SavedLocationEvent
}