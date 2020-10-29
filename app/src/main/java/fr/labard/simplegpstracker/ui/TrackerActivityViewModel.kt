package fr.labard.simplegpstracker.ui

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.RecordListFragmentViewModel
import fr.labard.simplegpstracker.model.data.AppRepository
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import java.util.*

class TrackerActivityViewModel(
    private val appRepository: IRepository
) : ViewModel() {
    val allRecords = appRepository.getRecords()
    val allLocations = appRepository.getLocations()

    var recordId = ""
    var isRecording = false

    fun getRecordById(id: String): RecordEntity {
        return allRecords.value?.find { it.id == id }!!
    }

    fun updateRecordName(name: String) {
        appRepository.updateRecordName(recordId, name)
        updateLastRecordModification()
    }

    fun updateLastRecordModification() {
        appRepository.updateLastRecordModification(recordId)
    }

    fun deleteRecord(recordId: String) = appRepository.deleteRecord(recordId)

    fun insertLocation(location: Location) {
        appRepository.insertLocation(
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

//    fun getLocationsByRecordId(recordId: Int): List<LocationEntity> = allLocations.value?.filter { it.recordId == recordId } ?: listOf()
}

@Suppress("UNCHECKED_CAST")
class TrackerActivityViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        TrackerActivityViewModelFactory(repository) as T
}