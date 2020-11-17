package fr.labard.simplegpstracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.labard.simplegpstracker.data.local.LocationEntity
import fr.labard.simplegpstracker.data.local.RecordEntity

class AppRepository internal constructor(
    private val localDataSource: IDataSource,
) : IRepository {


    override var activeRecordId = MutableLiveData("")

    override fun getRecords(): LiveData<List<RecordEntity>> {
        return localDataSource.getRecords()
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

    override fun insertLocation(locationEntity: LocationEntity) {
        localDataSource.insertLocation(locationEntity)
    }

    override fun deleteAll() {
        localDataSource.deleteAll()
    }
}