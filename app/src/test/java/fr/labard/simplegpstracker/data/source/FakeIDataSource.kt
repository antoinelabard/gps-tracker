package fr.labard.simplegpstracker.data.source

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.labard.simplegpstracker.data.IDataSource
import fr.labard.simplegpstracker.data.local.LocationEntity
import fr.labard.simplegpstracker.data.local.RecordEntity
import java.util.*

class FakeIDataSource internal constructor(
    records: MutableList<RecordEntity>,
    locations: MutableList<LocationEntity>
): IDataSource {

    private val allRecords = MutableLiveData(records)
    private val allLocations = MutableLiveData(locations)

    override fun getRecords() = allRecords as LiveData<List<RecordEntity>>

    override fun insertRecord(recordEntity: RecordEntity): AsyncTask<RecordEntity?, Void?, Void?>? {
        allRecords.value?.add(recordEntity)
        return null
    }

    override fun updateRecordName(id: String, name: String): AsyncTask<Map<String, String>, Void?, Void?>? {
        allRecords.value?.find { it.id == id }?.name = name
        return null
    }

    override fun updateLastRecordModification(id: String): AsyncTask<String, Void?, Void?>? {
        allRecords.value?.find { it.id == id }?.lastModification = Date()
        return null
    }

    override fun deleteRecord(id: String): AsyncTask<String?, Void?, Void?>? {
        allRecords.value?.removeIf { it.id == id }
        return null
    }

    override fun getLocations() = allLocations as LiveData<List<LocationEntity>>

    override fun insertLocation(locationEntity: LocationEntity): AsyncTask<LocationEntity?, Void?, Void?>? {
        allLocations.value?.add(locationEntity)
        return null
    }

    override fun deleteAll(): AsyncTask<Void?, Void?, Void?>? {
        allRecords.value?.clear()
        allLocations.value?.clear()
        return null
    }
}