package fr.labard.simplegpstracker

import android.content.Context
import android.os.AsyncTask
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import fr.labard.simplegpstracker.data.AppRepository
import fr.labard.simplegpstracker.data.IRepository
import fr.labard.simplegpstracker.data.local.*
import fr.labard.simplegpstracker.util.Constants
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
        val newRepository = AppRepository(createLocalDataSource(context))
        appRepository = newRepository
        return newRepository
    }

    private fun createLocalDataSource(context: Context): LocalDataSource {
        val database = database ?: createDataBase(context)
        return LocalDataSource(database.recordDao(), database.locationDao())
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


    private class PopulateDbAsync constructor(db: AppRoomDatabase?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val mRecordDao: RecordDao = db!!.recordDao()
        private val mLocationDao: LocationDao = db!!.locationDao()
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
            mRecordDao.deleteAll()
            for (element in records) {
                mRecordDao.insertRecord(element)
            }
            for (element in locations) {
                mLocationDao.insertLocation(element)
            }
            return null
        }
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            appRepository = null
        }
    }
}