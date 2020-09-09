package com.example.simplegpstracker.model.db

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.simplegpstracker.model.db.location.LocationDao
import com.example.simplegpstracker.model.db.location.LocationEntity
import com.example.simplegpstracker.model.db.record.RecordDao
import com.example.simplegpstracker.model.db.record.RecordEntity

class AppRepository internal constructor(application: Application?) {
    private val mRecordDao: RecordDao?
    private val mLocationDao: LocationDao?
    val allRecords: LiveData<List<RecordEntity>>
    val allLocations: LiveData<List<LocationEntity>>

    init {
        val db = AppRoomDatabase.getDatabase(application!!)
        mRecordDao = db!!.recordDao()
        mLocationDao = db.locationDao()
        allRecords = mRecordDao!!.getAll()
        allLocations = mLocationDao!!.getAll()
    }

    fun insertRecord(recordEntity: RecordEntity?) = insertRecordAsyncTask(mRecordDao).execute(recordEntity)
    fun deleteRecord(recordId: Int) = deleteRecordAsyncTask(mRecordDao).execute(recordId)
    fun insertLocation(locationEntity: LocationEntity?) = insertLocationAsyncTask(mLocationDao).execute(locationEntity)

    private class insertRecordAsyncTask internal constructor(private val mDao: RecordDao?) :
        AsyncTask<RecordEntity?, Void?, Void?>() {
        override fun doInBackground(vararg params: RecordEntity?): Void? {
            params[0]?.let { mDao!!.insert(it) }
            return null
        }
    }

    private class deleteRecordAsyncTask internal constructor(private val mDao: RecordDao?) :
        AsyncTask<Int?, Void?, Void?>() {
        override fun doInBackground(vararg params: Int?): Void? {
            params[0]?.let { mDao!!.deleteById(it) }
            return null
        }

    }

    private class insertLocationAsyncTask internal constructor(private val mDao: LocationDao?) :
        AsyncTask<LocationEntity?, Void?, Void?>() {
        override fun doInBackground(vararg params: LocationEntity?): Void? {
            params[0]?.let { mDao!!.insert(it) }
            return null
        }

    }
}