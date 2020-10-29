package fr.labard.simplegpstracker.model.data.local

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

    override fun getRecords(): LiveData<List<RecordEntity>> {
        return recordDao.getRecords()
    }

    override fun getRecordById(id: String): RecordEntity {
        TODO("Not yet implemented")
    }

    override fun insertRecord(recordEntity: RecordEntity) {
        recordDao.insertRecord(recordEntity)
    }

    override fun updateRecordName(id: String, name: String) {
        recordDao.updateRecordName(id, name)
    }

    override fun updateLastRecordModification(id: String) {
        recordDao.updateLastRecordModification(id, Date())
    }

    override fun deleteRecord(id: String) {
        recordDao.deleteRecord(id)
    }

    override fun getLocations(): LiveData<List<LocationEntity>> {
        return locationDao.getAll()
    }

    override fun getLocationsByRecordId(recordId: String) {
        TODO("Not yet implemented")
    }

    override fun insertLocation(locationEntity: LocationEntity) {
        locationDao.insert(locationEntity)
    }

    override fun deleteAll() {
        recordDao.deleteAll()
        locationDao.deleteAll()
    }
}