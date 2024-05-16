package com.ossipeeriver.ravenandroidawarenesskit.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [SavedLocation::class],
    version = 1
)
abstract class SavedLocationDatabase: RoomDatabase() {
    abstract val dao: SavedLocationDao
}