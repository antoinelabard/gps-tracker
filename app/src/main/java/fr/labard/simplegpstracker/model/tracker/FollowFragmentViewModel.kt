package fr.labard.simplegpstracker.model.tracker

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.model.data.IRepository

class FollowFragmentViewModel(
    private val appRepository: IRepository
) : ViewModel() {


    var currentRecordId = appRepository.getActiveRecordId()
    var allLocations = appRepository.getLocations()
    var locationsByRecordId = allLocations.value!!.filter { it.recordId == currentRecordId.value } as MutableList
    var currentLocation = Location("")
    var isRecording = false
}

@Suppress("UNCHECKED_CAST")
class FollowFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (FollowFragmentViewModel(repository) as T)
}