package fr.labard.simplegpstracker.model.data.local

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.model.data.DataSource
import fr.labard.simplegpstracker.model.data.local.db.location.LocationDao
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordDao
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.*

class LocalDataSource(
    private val recordDao: RecordDao,
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): DataSource {

    override fun getRecords(): LiveData<List<RecordEntity>> = recordDao.getRecords()

    override fun getRecord(id: String): LiveData<RecordEntity> = recordDao.getRecordById(id)

    override fun insertRecord(recordEntity: RecordEntity) = InsertRecordAsyncTask(recordDao).execute(recordEntity)

    override fun updateRecordName(id: String, name: String) = UpdateRecordNameAsyncTask(recordDao).execute(mapOf(id to name))

    override fun updateLastRecordModification(id: String) = UpdateLastRecordModificationAsyncTask(recordDao).execute(id)

    override fun deleteRecord(id: String) = DeleteRecordAsyncTask(recordDao).execute(id)

    override fun getLocations(): LiveData<List<LocationEntity>> = locationDao.getAll()

    override fun getLocationsByRecordId(recordId: String): LiveData<List<LocationEntity>> =
        locationDao.getLocationsByRecordId(recordId)

    override fun insertLocation(locationEntity: LocationEntity) = InsertLocationAsyncTask(locationDao).execute(locationEntity)

    override fun deleteAll() = DeleteAllAsyncTask(recordDao).execute()


    private class InsertRecordAsyncTask constructor(private val dao: RecordDao) :
        AsyncTask<RecordEntity?, Void?, Void?>() {
        override fun doInBackground(vararg params: RecordEntity?): Void? {
            params[0]?.let { dao.insertRecord(it) }
            return null
        }
    }

    private class UpdateRecordNameAsyncTask constructor(private val dao: RecordDao?) :
        AsyncTask<Map<String, String>, Void?, Void?>() {
        override fun doInBackground(vararg params: Map<String, String>): Void? {
            params[0].let {
                for ((id, name) in it) dao?.updateRecordName(id, name)
            }
            return null
        }
    }

    private class UpdateLastRecordModificationAsyncTask constructor(private val dao: RecordDao?) :
        AsyncTask<String, Void?, Void?>() {
        override fun doInBackground(vararg params: String?): Void? {
            params[0]?.let { dao?.updateLastRecordModification(it, Date()) }
            return null
        }
    }

    private class DeleteRecordAsyncTask constructor(private val dao: RecordDao?) :
        AsyncTask<String?, Void?, Void?>() {
        override fun doInBackground(vararg params: String?): Void? {
            params[0]?.let { dao!!.deleteRecord(it) }
            return null
        }
    }

    private class InsertLocationAsyncTask constructor(private val dao: LocationDao?) :
        AsyncTask<LocationEntity?, Void?, Void?>() {
        override fun doInBackground(vararg params: LocationEntity?): Void? {
            try {
                params[0]?.let { dao!!.insert(it) }
            } catch (e: Exception) {
                Log.e("OrphanLocationException","Exception: This location belongs to no record.")
            }
            return null
        }

    }

    private class DeleteAllAsyncTask constructor(private val dao: RecordDao?) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            dao!!.deleteAll()
            return null
        }
    }
}