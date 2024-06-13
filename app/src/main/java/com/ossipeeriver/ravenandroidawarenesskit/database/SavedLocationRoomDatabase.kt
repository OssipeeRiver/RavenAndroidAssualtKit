package com.ossipeeriver.ravenandroidawarenesskit.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [SavedLocation::class], version = 2)
abstract class SavedLocationRoomDatabase: RoomDatabase() {
    abstract fun savedLocationDao(): SavedLocationDao

    companion object {
        @Volatile
        private var INSTANCE: SavedLocationRoomDatabase? = null

        fun getDatabase( context: Context, scope: CoroutineScope): SavedLocationRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SavedLocationRoomDatabase::class.java,
                    "saved_location_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(SavedLocationDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class SavedLocationDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.savedLocationDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(savedLocationDao: SavedLocationDao) {
            // populate database with sample data
            val sampleLocationOne = SavedLocation(
                "40.0220, -100.0100",
                "Charlie Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus lacinia odio " +
                        "vitae vestibulum vestibulum. Cras venenatis euismod malesuada. Curabitur sollicitudin " +
                        "tincidunt metus, at interdum lorem vehicula sit amet. Maecenas tempor dolor quis eros auctor," +
                        " id pretium lacus aliquet. Sed quis orci sed lacus viverra commodo. Praesent dapibus sapien in " +
                        "odio faucibus, nec viverra magna vestibulum. Donec quis nulla nec lectus efficitur dignissim." +
                        " Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas." +
                        " In bibendum dui in metus pretium, et fermentum justo efficitur. Phasellus id massa sit amet turpis pharetra consequat.."
            )
            savedLocationDao.insertLocation(sampleLocationOne)

            val sampleLocationTwo = SavedLocation(
                "40.0220, -100.0100",
                "Alpha ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            )
            savedLocationDao.insertLocation(sampleLocationTwo)

            val sampleLocationThree = SavedLocation(
                "40.0220, -100.0100",
                "Bravo ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            )
            savedLocationDao.insertLocation(sampleLocationThree)
        }
    }
}

