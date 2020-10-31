package fr.labard.simplegpstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity

class MapFragmentViewModel(
    private val appRepository: IRepository
) : ViewModel() {

    var activeRecordId = appRepository.getActiveRecordId()
    var allLocations = appRepository.getLocations()
    var locationsByRecordId = listOf<LocationEntity>()

    fun getLocationsByRecordActiveId(): List<LocationEntity> = allLocations.value!!.filter { it.recordId == activeRecordId.value }
}

@Suppress("UNCHECKED_CAST")
class MapFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MapFragmentViewModel(repository) as T)
}