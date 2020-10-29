package fr.labard.simplegpstracker.model.data

import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity

interface IRepository {
    fun getRecords(): LiveData<List<RecordEntity>>
    fun getRecordById(id: String): RecordEntity
    fun insertRecord(recordEntity: RecordEntity)
    fun updateRecordName(id: String, name: String)
    fun updateLastRecordModification(id: String)
    fun deleteRecord(id: String)

    fun getLocations(): LiveData<List<LocationEntity>>
    fun getLocationsByRecordId(recordId: String)
    fun insertLocation(locationEntity: LocationEntity)

    fun deleteAll()
}