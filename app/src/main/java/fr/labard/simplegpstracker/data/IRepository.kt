package fr.labard.simplegpstracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.labard.simplegpstracker.data.local.LocationEntity
import fr.labard.simplegpstracker.data.local.RecordEntity

interface IRepository {

    var activeRecordId: MutableLiveData<String>

    fun getRecords(): LiveData<List<RecordEntity>>
    fun insertRecord(recordEntity: RecordEntity)
    fun updateRecordName(id: String, name: String)
    fun updateLastRecordModification(id: String)
    fun deleteRecord(id: String)

    fun getLocations(): LiveData<List<LocationEntity>>
    fun insertLocation(locationEntity: LocationEntity)

    fun deleteAll()
}