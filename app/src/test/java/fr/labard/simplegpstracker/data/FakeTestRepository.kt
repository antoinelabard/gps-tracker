package fr.labard.simplegpstracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.labard.simplegpstracker.data.local.LocationEntity
import fr.labard.simplegpstracker.data.local.RecordEntity
import fr.labard.simplegpstracker.data.source.FakeIDataSource

class FakeTestRepository(
    val fakeDataSource: FakeIDataSource
): IRepository {

    override var activeRecordId = MutableLiveData("")

    override fun getRecords(): LiveData<List<RecordEntity>> =
        fakeDataSource.getRecords()

    override fun insertRecord(recordEntity: RecordEntity) {
        fakeDataSource.insertRecord(recordEntity)
    }

    override fun updateRecordName(id: String, name: String) {
        fakeDataSource.updateRecordName(id, name)
    }

    override fun updateLastRecordModification(id: String) {
        fakeDataSource.updateLastRecordModification(id)
    }

    override fun deleteRecord(id: String) {
        fakeDataSource.deleteRecord(id)
    }

    override fun getLocations(): LiveData<List<LocationEntity>> =
        fakeDataSource.getLocations()

    override fun insertLocation(locationEntity: LocationEntity) {
        fakeDataSource.insertLocation(locationEntity)
    }

    override fun deleteAll() {
        fakeDataSource.deleteAll()
    }


/*
    val allRecords = hashMapOf<Int, RecordEntity>()

    val allLocations = hashMapOf<Int, LocationEntity>()

    override fun getRecords(): LiveData<List<RecordEntity>> = MutableLiveData(allRecords.values.toList())


    override fun insertRecord(recordEntity: RecordEntity): AsyncTask<RecordEntity?, Void?, Void?>? {
        if (allRecords.keys.contains(recordEntity!!.id)) {
            allRecords[recordEntity.id] = recordEntity
        }
        return null
    }

    override fun updateRecordName(id: Int, name: String): AsyncTask<Map<Int, String>, Void?, Void?>? {
        allRecords[id].apply { this?.name = name }
        return null
    }

    override fun updateLastRecordModification(id: Int): AsyncTask<Int, Void?, Void?>? {
        allRecords[id].apply { this?.lastModification = Date() }
        return null
    }

    override fun deleteRecord(id: Int): AsyncTask<Int?, Void?, Void?>? {
        allRecords.remove(id)
        return null
    }

    override fun getLocations(): LiveData<List<LocationEntity>> = MutableLiveData(allLocations.values.toList())

    override fun insertLocation(locationEntity: LocationEntity?): AsyncTask<LocationEntity?, Void?, Void?>? {
        if (allLocations.keys.contains(locationEntity!!.id)) {
            allLocations[locationEntity.id] = locationEntity
        }
        return null
    }*/
}
