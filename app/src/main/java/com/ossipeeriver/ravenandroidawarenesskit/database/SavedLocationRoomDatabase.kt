package com.ossipeeriver.ravenandroidawarenesskit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [SavedLocation::class], version = 1)
abstract class SavedLocationRoomDatabase: RoomDatabase() {
    abstract fun savedLocationDao(): SavedLocationDao

    companion object {
        @Volatile
        private var INSTANCE: SavedLocationRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): SavedLocationRoomDatabase {
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
        ) : RoomDatabase.Callback() {
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
            var sampleLocation = SavedLocation("123456 123456", "A sample location")
            savedLocationDao.upsertLocation(sampleLocation)
        }
    }
}

