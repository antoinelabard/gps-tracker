package fr.labard.simplegpstracker.model.db

import androidx.lifecycle.LiveData
import fr.labard.simplegpstracker.model.db.location.LocationEntity
import fr.labard.simplegpstracker.model.db.record.RecordEntity

class LocalDataSource: DataSource {
    override fun getRecords(): LiveData<List<RecordEntity>> {
        TODO("Not yet implemented")
    }

    override fun insertRecord(recordEntity: RecordEntity?) {
        TODO("Not yet implemented")
    }

    override fun updateRecordName(id: Int, name: String) {
        TODO("Not yet implemented")
    }

    override fun updateLastRecordModification(id: Int) {
        TODO("Not yet implemented")
    }

    override fun deleteRecord(recordId: Int) {
        TODO("Not yet implemented")
    }

    override fun getLocations(): LiveData<List<LocationEntity>> {
        TODO("Not yet implemented")
    }

    override fun insertLocation(locationEntity: LocationEntity?) {
        TODO("Not yet implemented")
    }
}