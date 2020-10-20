package fr.labard.simplegpstracker

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.model.db.IRepository
import fr.labard.simplegpstracker.model.db.location.LocationEntity
import fr.labard.simplegpstracker.model.db.record.RecordEntity

class FakeTestRepository: IRepository {
    override val allRecords: LiveData<List<RecordEntity>>
        get() = TODO("Not yet implemented")
    override val allLocations: LiveData<List<LocationEntity>>
        get() = TODO("Not yet implemented")

    override fun insertRecord(recordEntity: RecordEntity?): AsyncTask<RecordEntity?, Void?, Void?>? {
        TODO("Not yet implemented")
    }

    override fun updateRecordName(id: Int, name: String): AsyncTask<Map<Int, String>, Void?, Void?>? {
        TODO("Not yet implemented")
    }

    override fun updateLastRecordModification(id: Int): AsyncTask<Int, Void?, Void?>? {
        TODO("Not yet implemented")
    }

    override fun deleteRecord(recordId: Int): AsyncTask<Int?, Void?, Void?>? {
        TODO("Not yet implemented")
    }

    override fun insertLocation(locationEntity: LocationEntity?): AsyncTask<LocationEntity?, Void?, Void?>? {
        TODO("Not yet implemented")
    }
}