package fr.labard.simplegpstracker.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.data.IRepository
import fr.labard.simplegpstracker.data.local.LocationEntity

class MapFragmentViewModel(
    private val appRepository: IRepository
) : ViewModel() {

    var activeRecordId = appRepository.activeRecordId
    var allLocations = appRepository.getLocations()
    var locationsByRecordId = listOf<LocationEntity>()
    var isRecording = false

    fun setLocationsByActiveRecordId() {
        locationsByRecordId = allLocations.value!!
            .filter { it.recordId == activeRecordId.value }
            .sortedBy { it.time }
    }
}

@Suppress("UNCHECKED_CAST")
class MapFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MapFragmentViewModel(repository) as T)
}