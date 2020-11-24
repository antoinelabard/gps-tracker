package fr.labard.gpsgpi.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.gpsgpi.data.IRepository
import fr.labard.gpsgpi.data.local.LocationEntity
import fr.labard.gpsgpi.data.local.RecordEntity
import java.util.*

class TrackerActivityViewModel(
    private val appRepository: IRepository
) : ViewModel() {
    val allRecords = appRepository.getRecords()
    val allLocations = appRepository.getLocations()
    var activeRecordId: String? = null
    var serviceIsBound: Boolean = false

    fun getRecordById(id: String)
            = allRecords.value?.find { it.id == id }
        ?: RecordEntity("", Date(), Date())

    fun insertLocation(location: LocationEntity) {
        appRepository.insertLocation(
            location
        )
    }

    fun updateRecordName(name: String) {
        appRepository.updateRecordName(activeRecordId!!, name)
        updateLastRecordModification()
    }

    fun updateLastRecordModification() {
        appRepository.updateLastRecordModification(activeRecordId!!)
    }

    fun deleteRecord(recordId: String) = appRepository.deleteRecord(recordId)
}

@Suppress("UNCHECKED_CAST")
class TrackerActivityViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        TrackerActivityViewModel(repository) as T
}