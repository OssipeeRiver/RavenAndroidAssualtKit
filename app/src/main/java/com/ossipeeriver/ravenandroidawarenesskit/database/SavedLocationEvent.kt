package com.ossipeeriver.ravenandroidawarenesskit.database

import com.ossipeeriver.ravenandroidawarenesskit.SortType

sealed interface SavedLocationEvent {
    object SaveLocation: SavedLocationEvent
    data class SetLatitudeAndLongitude(val latitudeAndLongitude: String): SavedLocationEvent
    data class SetDescription(val description: String): SavedLocationEvent
    data class DeleteLocation(val savedLocation: SavedLocation): SavedLocationEvent
    data class SortSavedLocations(val sortType: SortType): SavedLocationEvent
}