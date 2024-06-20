package com.ossipeeriver.ravenandroidawarenesskit.ui.location

import android.app.Application
import com.ossipeeriver.ravenandroidawarenesskit.location_db.SavedLocationRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 *  creates database and repository instance based on DAO.
 *  database and repository are only created when they're needed rather than when the application starts
 *  by lazy
 */
class SavedLocationApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { SavedLocationRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { LocationRepository(database.savedLocationDao()) }

}