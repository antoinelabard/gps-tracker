package fr.labard.simplegpstracker.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.labard.simplegpstracker.AndroidData.Companion.le1
import fr.labard.simplegpstracker.AndroidData.Companion.le2
import fr.labard.simplegpstracker.AndroidData.Companion.r1
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import java.util.*

class FakeAndroidTestRepository : IRepository {

    override var activeRecordId = MutableLiveData("0")

    val allRecords = MutableLiveData(mutableListOf(r1))
    val allLocations = MutableLiveData(mutableListOf(le1, le2))

    override fun getRecords() = allRecords as LiveData<List<RecordEntity>>

    override fun insertRecord(recordEntity: RecordEntity) {
        allRecords.value?.add(recordEntity)
    }

    override fun updateRecordName(id: String, name: String) {
        allRecords.value?.find { it.id == id }?.name = name
    }

    override fun updateLastRecordModification(id: String) {
        allRecords.value?.find { it.id == id }?.lastModification = Date()
    }

    override fun deleteRecord(id: String) {
        allRecords.value?.removeIf { it.id == id }
    }

    override fun getLocations() = allLocations as LiveData<List<LocationEntity>>

    override fun insertLocation(locationEntity: LocationEntity) {
        allLocations.value?.add(locationEntity)
    }

    override fun deleteAll() {
        allRecords.value?.clear()
        allLocations.value?.clear()
    }

}
