package fr.labard.simplegpstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity

class MapFragmentViewModel(
    private val appRepository: IRepository
) : ViewModel() {

    val allLocations = appRepository.getLocations()
    var recordId = ""

    fun getLocationsByRecordId(recordId: String): List<LocationEntity> = allLocations.value?.filter { it.recordId == recordId } ?: listOf()


}

@Suppress("UNCHECKED_CAST")
class MapFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MapFragmentViewModelFactory(repository) as T)
}