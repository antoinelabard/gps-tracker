package fr.labard.simplegpstracker.tracker

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity

class TrackerActivityViewModel(
    private val appRepository: IRepository
) : ViewModel() {
    val allRecords = appRepository.getRecords()
    var activeRecordId = appRepository.getActiveRecordId()

    var isRecording = false

    fun getRecordById(id: String): RecordEntity {
        return allRecords.value?.find { it.id == id }!!
    }

    fun updateRecordName(name: String) {
        appRepository.updateRecordName(activeRecordId.value!!, name)
        updateLastRecordModification()
    }

    fun updateLastRecordModification() {
        appRepository.updateLastRecordModification(activeRecordId.value!!)
    }

    fun deleteRecord(recordId: String) = appRepository.deleteRecord(recordId)

    fun insertLocation(location: Location) {
        appRepository.insertLocation(
            LocationEntity(
                0,
                activeRecordId.value!!,
                location.time,
                location.latitude,
                location.longitude,
                location.speed
            )
        )
    }

    fun getActiveRecordId(): String = appRepository.getActiveRecordId().value!!

    fun setActiveRecordId(recordId: String) {
        appRepository.setActiveRecordId(recordId)
    }
}

@Suppress("UNCHECKED_CAST")
class TrackerActivityViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        TrackerActivityViewModel(repository) as T
}