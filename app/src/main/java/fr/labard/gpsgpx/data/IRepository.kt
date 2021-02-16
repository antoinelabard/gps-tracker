package fr.labard.gpsgpx.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.labard.gpsgpx.data.local.LocationEntity
import fr.labard.gpsgpx.data.local.RecordEntity

/**
 * Interface of the repository used in the application.
 */
interface IRepository {

    // used to keep in memory which record is being used
    var activeRecordId: MutableLiveData<String>

    // refers to either the application is in record, follow or standby mode
    var recordingMode: MutableLiveData<String>

    fun getRecords(): LiveData<List<RecordEntity>>
    fun insertRecord(recordEntity: RecordEntity)
    fun updateRecordName(id: String, name: String)
    fun updateLastRecordModification(id: String)
    fun deleteRecord(id: String)

    fun getLocations(): LiveData<List<LocationEntity>>
    fun insertLocation(locationEntity: LocationEntity)

    fun clearAll()
}