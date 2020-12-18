package fr.labard.gpsgpx.tracker

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.gpsgpx.data.IRepository
import fr.labard.gpsgpx.data.local.LocationEntity

class RecordFragmentViewModel(
    appRepository: IRepository
) : ViewModel() {

    var serviceIsBound = false
    var activeRecordId: String? = null
    var allLocations = appRepository.getLocations()
    var locationsByRecordId = listOf<LocationEntity>()
    var currentLocation = Location("")

    fun setLocationsByActiveRecordId() {
        activeRecordId?.let { id ->
            locationsByRecordId = allLocations.value?.filter { it.recordId == id } ?: listOf() }
    }
}

@Suppress("UNCHECKED_CAST")
class MapFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (RecordFragmentViewModel(repository) as T)
}