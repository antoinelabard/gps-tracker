package fr.labard.simplegpstracker.model.tracker

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity

class MapFragmentViewModel(
    private val appRepository: IRepository
) : ViewModel() {

    var currentRecordId = appRepository.getActiveRecordId()
    var allLocations = appRepository.getLocations()
    var locationsByRecordId = listOf<LocationEntity>()
    var isRecording = false

    fun setLocationsByRecordActiveId() {
        locationsByRecordId = allLocations.value!!.filter { it.recordId == currentRecordId.value }
    }

    fun insertLocation(location: Location) {
        appRepository.insertLocation(
            LocationEntity(
                currentRecordId.value!!,
                location.time,
                location.latitude,
                location.longitude,
                location.speed
            )
        )
    }
}

@Suppress("UNCHECKED_CAST")
class MapFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MapFragmentViewModel(repository) as T)
}