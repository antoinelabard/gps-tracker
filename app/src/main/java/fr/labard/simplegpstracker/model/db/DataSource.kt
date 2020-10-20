package fr.labard.simplegpstracker.model.db

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.model.db.location.LocationEntity
import fr.labard.simplegpstracker.model.db.record.RecordEntity

interface DataSource {
    fun getRecords(): LiveData<List<RecordEntity>>
    fun insertRecord(recordEntity: RecordEntity?)
    fun updateRecordName(id: Int, name: String)
    fun updateLastRecordModification(id: Int)
    fun deleteRecord(recordId: Int)

    fun getLocations(): LiveData<List<LocationEntity>>
    fun insertLocation(locationEntity: LocationEntity?)
}