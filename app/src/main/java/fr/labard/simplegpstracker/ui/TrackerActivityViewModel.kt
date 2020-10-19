package fr.labard.simplegpstracker.ui

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import com.example.simplegpstracker.model.db.AppRepository
import com.example.simplegpstracker.model.db.location.LocationEntity
import com.example.simplegpstracker.model.db.record.RecordEntity
import java.util.*

class TrackerActivityViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val mAppRepository = AppRepository(application)
    val allRecords = mAppRepository.allRecords
    val allLocations = mAppRepository.allLocations

    var recordId: Int = 0
    var isRecording = false

    fun getRecordById(id: Int): RecordEntity {
        val records = allRecords.value?.filter { it.id == id }!!
        if (records.isEmpty()) return RecordEntity(-1, "", Date(), Date())
        return records.first()
    }

    fun updateRecordName(name: String) {
        mAppRepository.updateRecordName(recordId, name)
        updateLastRecordModification()
    }

    fun updateLastRecordModification() {
        mAppRepository.updateLastRecordModification(recordId)
    }

    fun deleteRecord(recordId: Int) = mAppRepository.deleteRecord(recordId)

    fun insertLocation(location: Location) {
        mAppRepository.insertLocation(
            LocationEntity(
                0,
                recordId,
                location.time,
                location.latitude,
                location.longitude,
                location.speed
            )
        )
    }

    fun getLocationsByRecordId(recordId: Int): List<LocationEntity> = allLocations.value?.filter { it.recordId == recordId } ?: listOf()
}

