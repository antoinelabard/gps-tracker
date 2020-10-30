package fr.labard.simplegpstracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity

class MapFragmentViewModel(
    private val appRepository: IRepository
) : ViewModel() {

    var activeRecordId = appRepository.getActiveRecordId()
    var locations = appRepository.getLocations()

    fun getLocationsByRecordActiveId(): List<LocationEntity> = locations.value!!.filter { it.recordId == activeRecordId.value }

    fun setActiveRecordId(recordId: String) {
        appRepository.setActiveRecordId(recordId)
    }
}

@Suppress("UNCHECKED_CAST")
class MapFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MapFragmentViewModel(repository) as T)
}