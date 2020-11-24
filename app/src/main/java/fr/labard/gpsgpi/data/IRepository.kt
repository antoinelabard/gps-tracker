package fr.labard.gpsgpi.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.labard.gpsgpi.data.local.LocationEntity
import fr.labard.gpsgpi.data.local.RecordEntity

/**
 * Interface of the repository used in the application.
 */
interface IRepository {

    // used to keep in memory which record is being used
    var activeRecordId: MutableLiveData<String>

    fun getRecords(): LiveData<List<RecordEntity>>
    fun insertRecord(recordEntity: RecordEntity)
    fun updateRecordName(id: String, name: String)
    fun updateLastRecordModification(id: String)
    fun deleteRecord(id: String)

    fun getLocations(): LiveData<List<LocationEntity>>
    fun insertLocation(locationEntity: LocationEntity)

    fun clearAll()
}