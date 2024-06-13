package com.ossipeeriver.ravenandroidawarenesskit.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "saved_locations")
data class SavedLocation(
    val latitudeAndLongitude: String,
    val description: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
