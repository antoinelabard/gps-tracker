package fr.labard.simplegpstracker.model.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity

class StatisticsFragmentViewModel(
    private val appRepository: IRepository
) : ViewModel() {
    val activeRecordId = appRepository.getActiveRecordId()
    val allLocations = appRepository.getLocations()
    var locationsByRecordId = listOf<LocationEntity>()

    fun setLocationsByRecordId() {
        locationsByRecordId = allLocations.value!!.filter { it.recordId == activeRecordId.value!! }
    }
}

@Suppress("UNCHECKED_CAST")
class StatisticsFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (StatisticsFragmentViewModel(repository) as T)
}