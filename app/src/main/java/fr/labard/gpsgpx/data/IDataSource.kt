package fr.labard.gpsgpx.data

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import fr.labard.gpsgpx.data.local.LocationEntity
import fr.labard.gpsgpx.data.local.RecordEntity

/**
 * Interface of the data sources used in the application.
 */
interface IDataSource {

    fun getRecords(): LiveData<List<RecordEntity>>
    fun insertRecord(recordEntity: RecordEntity): AsyncTask<RecordEntity?, Void?, Void?>?
    fun updateRecordName(id: String, name: String): AsyncTask<Map<String, String>, Void?, Void?>?
    fun updateLastRecordModification(id: String): AsyncTask<String, Void?, Void?>?
    fun deleteRecord(id: String): AsyncTask<String?, Void?, Void?>?

    fun getLocations(): LiveData<List<LocationEntity>>
    fun insertLocation(locationEntity: LocationEntity): AsyncTask<LocationEntity?, Void?, Void?>?

    fun clearAll(): AsyncTask<Void?, Void?, Void?>?
}