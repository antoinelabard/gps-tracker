package fr.labard.simplegpstracker.model.data

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.model.data.local.db.AppRoomDatabase
import fr.labard.simplegpstracker.model.data.local.db.location.LocationDao
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordDao
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import java.util.*

class AppRepository internal constructor(application: Application?) : IRepository {
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

    override fun getRecords(): LiveData<List<RecordEntity>> = mRecordDao!!.getAll()
    override fun insertRecord(recordEntity: RecordEntity?) = InsertRecordAsyncTask(mRecordDao).execute(recordEntity)
    override fun updateRecordName(id: Int, name: String) = UpdateNameRecordAsyncTask(mRecordDao).execute(mapOf(id to name))
    override fun updateLastRecordModification(id: Int) = UpdateLastRecordModificationAsyncTask(mRecordDao).execute(id)
    override fun deleteRecord(recordId: Int) = DeleteRecordAsyncTask(mRecordDao).execute(recordId)
    override fun getLocations(): LiveData<List<LocationEntity>> = mLocationDao!!.getAll()
    override fun insertLocation(locationEntity: LocationEntity?) = InsertLocationAsyncTask(mLocationDao).execute(locationEntity)

    private class InsertRecordAsyncTask constructor(private val mDao: RecordDao?) :
        AsyncTask<RecordEntity?, Void?, Void?>() {
        override fun doInBackground(vararg params: RecordEntity?): Void? {
            params[0]?.let { mDao!!.insert(it) }
            return null
        }
    }
    private class UpdateNameRecordAsyncTask constructor(private val mDao: RecordDao?) :
        AsyncTask<Map<Int, String>, Void?, Void?>() {
        override fun doInBackground(vararg params: Map<Int, String>): Void? {
            params[0].let {
                for ((id, name) in it) mDao?.updateName(id, name)
            }
            return null
        }
    }
    private class UpdateLastRecordModificationAsyncTask constructor(private val mDao: RecordDao?) :
        AsyncTask<Int, Void?, Void?>() {
        override fun doInBackground(vararg params: Int?): Void? {
            params[0]?.let { mDao?.updateLastRecordModification(it, Date()) }
            return null
        }
    }

    private class DeleteRecordAsyncTask constructor(private val mDao: RecordDao?) :
        AsyncTask<Int?, Void?, Void?>() {
        override fun doInBackground(vararg params: Int?): Void? {
            params[0]?.let { mDao!!.deleteById(it) }
            return null
        }

    }

    private class InsertLocationAsyncTask constructor(private val mDao: LocationDao?) :
        AsyncTask<LocationEntity?, Void?, Void?>() {
        override fun doInBackground(vararg params: LocationEntity?): Void? {
            try {
                params[0]?.let { mDao!!.insert(it) }
            } catch (e: Exception) {
                Log.e("OrphanLocationException","Exception: This location belongs to no record.")
            }
            return null
        }

    }
}