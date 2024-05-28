package com.ossipeeriver.ravenandroidawarenesskit.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface SavedLocationDao {
    @Upsert
    suspend fun upsertLocation(savedLocation: SavedLocation)

    @Delete
    suspend fun deleteLocation(savedLocation: SavedLocation)

    @Query("SELECT * FROM savedlocation ORDER BY description ASC")
    fun getSavedLocationByDescription(): Flow<List<SavedLocation>>

    @Query("SELECT * FROM savedlocation ORDER BY latitudeAndLongitude ASC")
    fun getSavedLocationByLatitudeAndLongitude(): Flow<List<SavedLocation>>

}