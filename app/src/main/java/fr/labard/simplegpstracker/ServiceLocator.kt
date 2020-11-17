package fr.labard.simplegpstracker

import android.content.Context
import android.os.AsyncTask
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import fr.labard.simplegpstracker.model.data.AppRepository
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.LocalDataSource
import fr.labard.simplegpstracker.model.data.local.db.AppRoomDatabase
import fr.labard.simplegpstracker.model.data.local.db.location.LocationDao
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordDao
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import fr.labard.simplegpstracker.model.util.Constants
import java.util.*

object ServiceLocator {

    private val lock = Any()
    private var database: AppRoomDatabase? = null
    @Volatile
    var appRepository: IRepository? = null
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
            AppRoomDatabase::class.java, Constants.Database.DATABASE_NAME
        ).build()
        database = result
        PopulateDbAsync(result)
        return result
    }


    private class PopulateDbAsync internal constructor(db: AppRoomDatabase?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val mRecordDao: RecordDao? = db!!.recordDao()
        private val mLocationDao: LocationDao? = db!!.locationDao()
        var records = arrayOf(
            RecordEntity("Record 1", Date(1577836800000), Date(1590969600000)),
            RecordEntity("Record 2", Date(1546300800000), Date(1559347200000)),
            RecordEntity("Record 3", Date(1514764800000), Date(1527811200000))
        )
        var locations = arrayOf(
            LocationEntity("0", 944006400000, 1.0, 2.0, 0.0f),
            LocationEntity("1", 944006405000, 2.0, 3.0, 0.0f),
            LocationEntity("1", 944006410000, 3.0, 4.0, 0.0f),
            LocationEntity("2", 944006415000, 4.0, 5.0, 0.0f),
            LocationEntity("2", 944006420000, 5.0, 6.0, 0.0f),
            LocationEntity("2", 944006425000, 6.0, 7.0, 0.0f)
        )
        override fun doInBackground(vararg params: Void?): Void? {
            mRecordDao!!.deleteAll()
            mLocationDao!!.deleteAll()
            for (element in records) {
                mRecordDao.insertRecord(element)
            }
            for (element in locations) {
                mLocationDao.insert(element)
            }
            return null
        }
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