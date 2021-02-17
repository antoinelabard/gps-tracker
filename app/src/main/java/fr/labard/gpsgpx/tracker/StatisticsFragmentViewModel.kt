package fr.labard.gpsgpx.tracker

import android.provider.Settings.Global.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.data.IRepository
import fr.labard.gpsgpx.data.local.LocationEntity
import fr.labard.gpsgpx.util.StatisticsPresenter

class StatisticsFragmentViewModel(
    appRepository: IRepository
) : ViewModel() {
    var activeRecordId: String? = null
    val allLocations = appRepository.getLocations()
    var locationsByRecordId = listOf<LocationEntity>()
    var serviceIsBound = false

    var stats = Statistics()

    inner class Statistics {
        val totalDistance
            get() = StatisticsPresenter.getTotalDistanceFormatted(locationsByRecordId)
        val totalTime
            get() = StatisticsPresenter.getTotalTimeFormatted(locationsByRecordId)
        val recentSpeed
        get() = StatisticsPresenter.getRecentSPeedFormatted(locationsByRecordId)
        val averageSpeed
            get() = StatisticsPresenter.getAverageSpeedFormatted(locationsByRecordId)
        val minSpeed
            get() = StatisticsPresenter.getMinSpeedFormatted(locationsByRecordId)
        val maxSpeed
            get() = StatisticsPresenter.getMaxSpeedFormatted(locationsByRecordId)
        val nbLocations
            get() = StatisticsPresenter.getNbLocationsPresented(locationsByRecordId)
    }

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