package fr.labard.gpsgpx.tracker

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

    fun setLocationsByActiveRecordId() {
        activeRecordId?.let { id ->
            locationsByRecordId = allLocations.value?.filter { it.recordId == id } ?: listOf() }
    }

    fun updateStatisticsDisplay() {
        fragment_statistics_textview_total_distance.text = getString(R.string.stats_total_distance).format(
            StatisticsPresenter.getTotalDistanceFormatted(viewModel.locationsByRecordId)
        )
        fragment_statistics_textview_total_time.text = getString(R.string.stats_total_time).format(
            StatisticsPresenter.getTotalTimeFormatted(viewModel.locationsByRecordId)
        )
        fragment_statistics_textview_recent_speed.text = getString(R.string.stats_recent_speed).format(
            StatisticsPresenter.getRecentSPeedFormatted(viewModel.locationsByRecordId)
        )
        fragment_statistics_textview_average_speed.text = getString(R.string.stats_average_speed).format(
            StatisticsPresenter.getAverageSpeedFormatted(viewModel.locationsByRecordId)
        )
        fragment_statistics_textview_min_speed.text = getString(R.string.stats_min_speed).format(
            StatisticsPresenter.getMinSpeedFormatted(viewModel.locationsByRecordId)
        )
        fragment_statistics_textview_max_speed.text = getString(R.string.stats_max_speed).format(
            StatisticsPresenter.getMaxSpeedFormatted(viewModel.locationsByRecordId)
        )
        fragment_statistics_textview_nb_locations.text = getString(R.string.stats_nb_locations).format(
            StatisticsPresenter.getNbLocationsPresented(viewModel.locationsByRecordId)
        )
    }
}

@Suppress("UNCHECKED_CAST")
class StatisticsFragmentViewModelFactory (
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (StatisticsFragmentViewModel(repository) as T)
}