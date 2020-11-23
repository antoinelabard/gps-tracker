package fr.labard.simplegpstracker.tracker

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.simplegpstracker.data.IRepository

class FollowFragmentViewModel(
    appRepository: IRepository
) : ViewModel() {

    var activeRecordId = appRepository.activeRecordId
    var allLocations = appRepository.getLocations()
    var locationsByRecordId = mutableListOf<Location>()
    var currentLocation = Location("")
    var isFollowing = false
}

@Suppress("UNCHECKED_CAST")
class FollowFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (FollowFragmentViewModel(repository) as T)
}