package com.ossipeeriver.ravenandroidawarenesskit.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface SavedLocationDao {
    @Insert
    suspend fun insertLocation(savedLocation: SavedLocation)

    @Delete
    suspend fun deleteLocation(savedLocation: SavedLocation)

    @Update
    suspend fun updateSavedLocation(savedLocation: SavedLocation)

    @Query("SELECT * FROM saved_locations ORDER BY description ASC")
    fun getSavedLocationByDescription(): Flow<List<SavedLocation>>

    @Query("SELECT * FROM saved_locations ORDER BY latitudeAndLongitude ASC")
    fun getSavedLocationByLatitudeAndLongitude(): Flow<List<SavedLocation>>

    @Query("SELECT * FROM saved_locations ORDER BY id ASC")
    fun getSavedLocationById(): Flow<List<SavedLocation>>

    @Query("DELETE FROM saved_locations")
    suspend fun deleteAll()

}