package com.ossipeeriver.ravenandroidawarenesskit.database

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class SavedLocation(
    val latitude: Double,
    val longitude: Double,
    val description: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
