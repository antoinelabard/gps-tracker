package fr.labard.simplegpstracker.model.data

import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.model.data.local.LocalDataSource
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppRepository internal constructor(
    private val localDataSource: LocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IRepository {
//    private val mRecordDao: RecordDao?
//    private val mLocationDao: LocationDao?
//    val allRecords: LiveData<List<RecordEntity>>
//    val allLocations: LiveData<List<LocationEntity>>
//
//    init {
//        val db = AppRoomDatabase.getDatabase(application!!)
//        mRecordDao = db!!.recordDao()
//        mLocationDao = db.locationDao()
//        allRecords = mRecordDao!!.getAll()
//        allLocations = mLocationDao!!.getAll()
//    }

    override fun getRecords(): LiveData<List<RecordEntity>> {
        return localDataSource.getRecords()
    }

    override fun getRecordById(id: String): RecordEntity {
        TODO("Not yet implemented")
    }

    override fun insertRecord(recordEntity: RecordEntity) {
        localDataSource.insertRecord(recordEntity)
    }

    override fun updateRecordName(id: String, name: String) {
        localDataSource.updateRecordName(id, name)
    }

    override fun updateLastRecordModification(id: String) {
        localDataSource.updateLastRecordModification(id)
    }

    override fun deleteRecord(id: String) {
        localDataSource.deleteRecord(id)
    }

    override fun getLocations(): LiveData<List<LocationEntity>> {
        return localDataSource.getLocations()
    }

    override fun getLocationsByRecordId(recordId: String) {
        TODO("Not yet implemented")
    }

    override fun insertLocation(locationEntity: LocationEntity) {
        localDataSource.insertLocation(locationEntity)
    }

    override fun deleteAll() {
        localDataSource.deleteAll()
    }

/*
The code below should be deleted if the repository works with the new implementation
*/
//    private class InsertRecordAsyncTask constructor(private val mDao: RecordDao?) :
//        AsyncTask<RecordEntity?, Void?, Void?>() {
//        override fun doInBackground(vararg params: RecordEntity?): Void? {
//            params[0]?.let { mDao!!.insert(it) }
//            return null
//        }
//    }
//    private class UpdateNameRecordAsyncTask constructor(private val mDao: RecordDao?) :
//        AsyncTask<Map<Int, String>, Void?, Void?>() {
//        override fun doInBackground(vararg params: Map<Int, String>): Void? {
//            params[0].let {
//                for ((id, name) in it) mDao?.updateName(id, name)
//            }
//            return null
//        }
//    }
//    private class UpdateLastRecordModificationAsyncTask constructor(private val mDao: RecordDao?) :
//        AsyncTask<Int, Void?, Void?>() {
//        override fun doInBackground(vararg params: Int?): Void? {
//            params[0]?.let { mDao?.updateLastRecordModification(it, Date()) }
//            return null
//        }
//    }
//
//    private class DeleteRecordAsyncTask constructor(private val mDao: RecordDao?) :
//        AsyncTask<Int?, Void?, Void?>() {
//        override fun doInBackground(vararg params: Int?): Void? {
//            params[0]?.let { mDao!!.deleteById(it) }
//            return null
//        }
//
//    }
//
//    private class InsertLocationAsyncTask constructor(private val mDao: LocationDao?) :
//        AsyncTask<LocationEntity?, Void?, Void?>() {
//        override fun doInBackground(vararg params: LocationEntity?): Void? {
//            try {
//                params[0]?.let { mDao!!.insert(it) }
//            } catch (e: Exception) {
//                Log.e("OrphanLocationException","Exception: This location belongs to no record.")
//            }
//            return null
//        }
//
//    }
}