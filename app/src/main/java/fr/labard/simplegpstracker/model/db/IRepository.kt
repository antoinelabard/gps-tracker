package fr.labard.simplegpstracker.model.db

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.model.db.location.LocationEntity
import fr.labard.simplegpstracker.model.db.record.RecordEntity

interface IRepository {
    val allRecords: LiveData<List<RecordEntity>>
    val allLocations: LiveData<List<LocationEntity>>
    fun insertRecord(recordEntity: RecordEntity?): AsyncTask<RecordEntity?, Void?, Void?>?
    fun updateRecordName(id: Int, name: String): AsyncTask<Map<Int, String>, Void?, Void?>?
    fun updateLastRecordModification(id: Int): AsyncTask<Int, Void?, Void?>?
    fun deleteRecord(recordId: Int): AsyncTask<Int?, Void?, Void?>?
    fun insertLocation(locationEntity: LocationEntity?): AsyncTask<LocationEntity?, Void?, Void?>?
}