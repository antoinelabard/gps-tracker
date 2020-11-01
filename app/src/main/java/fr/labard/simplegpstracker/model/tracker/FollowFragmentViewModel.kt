package fr.labard.simplegpstracker.model.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity

class FollowFragmentViewModel(
    private val appRepository: IRepository
) : ViewModel() {

    var activeRecordId = appRepository.getActiveRecordId()
    var allLocations = appRepository.getLocations()
    var locationsByRecordId = listOf<LocationEntity>()

    fun setLocationsByRecordActiveId() {
        locationsByRecordId = allLocations.value!!.filter { it.recordId == activeRecordId.value }
    }
}

@Suppress("UNCHECKED_CAST")
class FollowFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (FollowFragmentViewModel(repository) as T)
}