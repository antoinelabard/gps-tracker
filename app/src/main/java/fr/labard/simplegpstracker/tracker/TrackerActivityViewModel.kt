package fr.labard.simplegpstracker.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.data.IRepository
import fr.labard.simplegpstracker.data.local.RecordEntity
import java.util.*

class TrackerActivityViewModel(
    private val appRepository: IRepository
) : ViewModel() {
    val allRecords = appRepository.getRecords()
    val allLocations = appRepository.getLocations()
    var activeRecordId = appRepository.activeRecordId

    fun getRecordById(id: String)
            = allRecords.value?.find { it.id == id }
        ?: RecordEntity("", Date(), Date())

    fun updateRecordName(name: String) {
        appRepository.updateRecordName(activeRecordId.value!!, name)
        updateLastRecordModification()
    }

    fun updateLastRecordModification() {
        appRepository.updateLastRecordModification(activeRecordId.value!!)
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