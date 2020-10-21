package fr.labard.simplegpstracker.model.data

import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity

interface IRepository {
    fun getRecords(): LiveData<List<RecordEntity>>
    fun insertRecord(recordEntity: RecordEntity)
    fun updateRecordName(id: Int, name: String)
    fun updateLastRecordModification(id: Int)
    fun deleteRecord(recordId: Int)

    fun getLocations(): LiveData<List<LocationEntity>>
    fun insertLocation(locationEntity: LocationEntity)

    fun deleteAll()
}