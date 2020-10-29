package fr.labard.simplegpstracker.model.data

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity

interface DataSource {
    fun getRecords(): LiveData<List<RecordEntity>>
    fun getRecord(id: String): LiveData<RecordEntity>
    fun insertRecord(recordEntity: RecordEntity): AsyncTask<RecordEntity?, Void?, Void?>?
    fun updateRecordName(id: String, name: String): AsyncTask<Map<String, String>, Void?, Void?>?
    fun updateLastRecordModification(id: String): AsyncTask<String, Void?, Void?>?
    fun deleteRecord(id: String): AsyncTask<String?, Void?, Void?>?

    fun getLocations(): LiveData<List<LocationEntity>>
    fun getLocationsByRecordId(recordId: String): LiveData<List<LocationEntity>>
    fun insertLocation(locationEntity: LocationEntity): AsyncTask<LocationEntity?, Void?, Void?>?

    fun deleteAll(): AsyncTask<Void?, Void?, Void?>?
}