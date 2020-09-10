package com.example.simplegpstracker.model.db

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.simplegpstracker.model.db.location.LocationDao
import com.example.simplegpstracker.model.db.location.LocationEntity
import com.example.simplegpstracker.model.db.record.RecordDao
import com.example.simplegpstracker.model.db.record.RecordEntity
import java.util.*

@Database(entities = [RecordEntity::class, LocationEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao?
    abstract fun locationDao(): LocationDao?

    private class PopulateDbAsync internal constructor(db: AppRoomDatabase?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val mRecordDao: RecordDao? = db!!.recordDao()
        private val mLocationDao: LocationDao? = db!!.locationDao()
        var records = arrayOf(
            RecordEntity(0, "Record 1", Date(1577836800000), Date(1590969600000)),
            RecordEntity(1, "Record 2", Date(1546300800000), Date(1559347200000)),
            RecordEntity(2, "Record 3", Date(1514764800000), Date(1527811200000))
        )
        var locations = arrayOf(
            LocationEntity(0, 0, 1.0, 2.0, 0.0f),
            LocationEntity(0, 1, 2.0, 3.0, 0.0f),
            LocationEntity(0, 1, 3.0, 4.0, 0.0f),
            LocationEntity(0, 2, 4.0, 5.0, 0.0f),
            LocationEntity(0, 2, 5.0, 6.0, 0.0f),
            LocationEntity(0, 2, 6.0, 7.0, 0.0f)
        )
        override fun doInBackground(vararg params: Void?): Void? {
            mRecordDao!!.deleteAll()
            mLocationDao!!.deleteAll()
            for (element in records) {
                mRecordDao.insert(element)
            }
            for (element in locations) {
                mLocationDao.insert(element)
            }
            return null
        }
    }

    companion object {
        private var INSTANCE: AppRoomDatabase? = null
        fun getDatabase(context: Context): AppRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(AppRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppRoomDatabase::class.java, "app_database"
                        )
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        /**
         * Override the onOpen method to populate the database.
         * For this sample, we clear the database every time it is created or opened.
         */
        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
//                PopulateDbAsync(
//                    INSTANCE
//                )
//                    .execute()
            }
        }
    }
}