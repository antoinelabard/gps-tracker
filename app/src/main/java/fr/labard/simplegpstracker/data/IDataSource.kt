package fr.labard.simplegpstracker.data

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.data.local.LocationEntity
import fr.labard.simplegpstracker.data.local.RecordEntity

interface IDataSource {
    fun getRecords(): LiveData<List<RecordEntity>>
    fun insertRecord(recordEntity: RecordEntity): AsyncTask<RecordEntity?, Void?, Void?>?
    fun updateRecordName(id: String, name: String): AsyncTask<Map<String, String>, Void?, Void?>?
    fun updateLastRecordModification(id: String): AsyncTask<String, Void?, Void?>?
    fun deleteRecord(id: String): AsyncTask<String?, Void?, Void?>?

    fun getLocations(): LiveData<List<LocationEntity>>
    fun insertLocation(locationEntity: LocationEntity): AsyncTask<LocationEntity?, Void?, Void?>?

    fun deleteAll(): AsyncTask<Void?, Void?, Void?>?
}