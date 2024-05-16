package com.ossipeeriver.ravenandroidawarenesskit.database

import com.ossipeeriver.ravenandroidawarenesskit.SortType

data class SavedLocationState(
    val savedLocation: List<SavedLocation> = emptyList(),
    val latitude: Double = 0.0, // TODO possible logical error
    val longitude: Double = 0.0, // TODO possible logical error
    val description: String = "",
    val isAddingLocation: Boolean = false,
    val sortType: SortType = SortType.LATITUDE // TODO sort by most recent instead?
)
