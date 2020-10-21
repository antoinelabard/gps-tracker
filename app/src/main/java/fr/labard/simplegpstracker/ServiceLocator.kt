package fr.labard.simplegpstracker

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import fr.labard.simplegpstracker.model.data.AppRepository
import fr.labard.simplegpstracker.model.data.local.db.AppRoomDatabase
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.LocalDataSource
import kotlinx.coroutines.runBlocking

object ServiceLocator {

    private val lock = Any()
    private var database: AppRoomDatabase? = null
    @Volatile
    var appRepository: AppRepository? = null
        @VisibleForTesting set

    fun provideAppRepository(context: Context): IRepository {
        synchronized(this) {
            return appRepository ?: createAppRepository(context)
        }
    }

    private fun createAppRepository(context: Context): IRepository {
        val newRepo = AppRepository(createLocalDataSource(context))
        appRepository = newRepo
        return newRepo
    }

    private fun createLocalDataSource(context: Context): LocalDataSource {
        val database = database ?: createDataBase(context)
        return LocalDataSource(database.recordDao()!!, database.locationDao()!!)
    }

    private fun createDataBase(context: Context): AppRoomDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            AppRoomDatabase::class.java, "Tasks.db"
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
//            runBlocking { // Not needed for the moment
//                RemoteDataSource().deleteAll()
//            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            appRepository = null
        }
    }
}