package fr.labard.gpsgpx.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.gpsgpx.data.IRepository
import fr.labard.gpsgpx.data.local.LocationEntity

class StatisticsFragmentViewModel(
    appRepository: IRepository
) : ViewModel() {
    var activeRecordId: String? = null
    val allLocations = appRepository.getLocations()
    var locationsByRecordId = listOf<LocationEntity>()
    var serviceIsBound = false

    fun setLocationsByActiveRecordId() {
        activeRecordId?.let { id ->
            locationsByRecordId = allLocations.value?.filter { it.recordId == id } ?: listOf() }
    }
}

@Suppress("UNCHECKED_CAST")
class StatisticsFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (StatisticsFragmentViewModel(repository) as T)
}