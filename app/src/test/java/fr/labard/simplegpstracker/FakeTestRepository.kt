package fr.labard.simplegpstracker

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import java.util.*

class FakeTestRepository: IRepository {
    override fun getRecords(): LiveData<List<RecordEntity>> {
        TODO("Not yet implemented")
    }

    override fun getRecordById(id: String): LiveData<RecordEntity> {
        TODO("Not yet implemented")
    }

    override fun insertRecord(recordEntity: RecordEntity) {
        TODO("Not yet implemented")
    }

    override fun updateRecordName(id: String, name: String) {
        TODO("Not yet implemented")
    }

    override fun updateLastRecordModification(id: String) {
        TODO("Not yet implemented")
    }

    override fun deleteRecord(id: String) {
        TODO("Not yet implemented")
    }

    override fun getLocations(): LiveData<List<LocationEntity>> {
        TODO("Not yet implemented")
    }

    override fun getLocationsByRecordId(recordId: String): LiveData<List<LocationEntity>> {
        TODO("Not yet implemented")
    }

    override fun insertLocation(locationEntity: LocationEntity) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
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
