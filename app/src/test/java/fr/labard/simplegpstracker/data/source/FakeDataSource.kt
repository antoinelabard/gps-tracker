package fr.labard.simplegpstracker.data.source

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.model.data.DataSource
import fr.labard.simplegpstracker.model.data.local.db.location.LocationDao
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordDao
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity

class FakeDataSource internal constructor(
    private val recordDao: RecordDao,
    private val locationDao: LocationDao,
): DataSource {

    override fun getRecords(): LiveData<List<RecordEntity>> {
        TODO("Not yet implemented")
    }

    override fun insertRecord(recordEntity: RecordEntity): AsyncTask<RecordEntity?, Void?, Void?>? {
        TODO("Not yet implemented")
    }

    override fun updateRecordName(id: String, name: String): AsyncTask<Map<String, String>, Void?, Void?>? {
        TODO("Not yet implemented")
    }

    override fun updateLastRecordModification(id: String): AsyncTask<String, Void?, Void?>? {
        TODO("Not yet implemented")
    }

    override fun deleteRecord(id: String): AsyncTask<String?, Void?, Void?>? {
        TODO("Not yet implemented")
    }

    override fun getLocations(): LiveData<List<LocationEntity>> {
        TODO("Not yet implemented")
    }

    override fun insertLocation(locationEntity: LocationEntity): AsyncTask<LocationEntity?, Void?, Void?>? {
        TODO("Not yet implemented")
    }

    override fun deleteAll(): AsyncTask<Void?, Void?, Void?>? {
        TODO("Not yet implemented")
    }

}