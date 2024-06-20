package com.ossipeeriver.ravenandroidawarenesskit.location_db

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "saved_locations")
data class SavedLocation(
    val latitudeAndLongitude: String,
    val description: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
