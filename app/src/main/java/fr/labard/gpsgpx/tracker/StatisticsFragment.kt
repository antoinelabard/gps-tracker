package fr.labard.gpsgpx.tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import fr.labard.gpsgpx.GPSApplication
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.util.StatisticsPresenter
import kotlinx.android.synthetic.main.fragment_statistics.*

/**
 * StatisticsFragment is used to display real time statistics about the active record.
 */
class StatisticsFragment : DialogFragment() {

    private val viewModel by viewModels<StatisticsFragmentViewModel> {
        StatisticsFragmentViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)
        dialog?.setTitle(getString(R.string.statistics_title))

        viewModel.allLocations.observe(viewLifecycleOwner, {
            viewModel.setLocationsByRecordId()
            updateStatisticsDisplay()
        })

        return view
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

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        @JvmStatic
        fun newInstance(sectionNumber: Int) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
    }
}