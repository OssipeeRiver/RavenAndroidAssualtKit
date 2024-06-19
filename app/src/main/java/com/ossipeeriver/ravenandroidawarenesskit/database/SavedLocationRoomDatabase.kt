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
                "Take notes on areas of interest. They can be as long as needed, however, you'll likely want to keep them as concise possible."
            )
            savedLocationDao.insertLocation(sampleLocationOne)

            val sampleLocationTwo = SavedLocation(
                "62.839993, -148.998786",
                "No signs of recent activity. Covered and concealed. Away from natural lines of drift. Defendable for short period of time."
            )
            savedLocationDao.insertLocation(sampleLocationTwo)

            val sampleLocationThree = SavedLocation(
                "35.572296, 8.756501",
                "Bravo ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            )
            savedLocationDao.insertLocation(sampleLocationThree)
        }
    }
}

