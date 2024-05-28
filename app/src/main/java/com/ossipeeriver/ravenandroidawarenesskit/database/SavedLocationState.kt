package com.ossipeeriver.ravenandroidawarenesskit.database

import com.ossipeeriver.ravenandroidawarenesskit.SortType

data class SavedLocationState(
    val savedLocation: List<SavedLocation> = emptyList(),
    val latitudeAndLongitude: String = "",
    val description: String = "",
    val isAddingLocation: Boolean = false,
    val sortType: SortType = SortType.LATITUDE_AND_LONGITUDE // TODO sort by most recent instead?
)
