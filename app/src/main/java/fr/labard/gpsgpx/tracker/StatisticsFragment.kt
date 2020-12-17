package fr.labard.gpsgpx.tracker

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
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
    private lateinit var gpsService: GpsService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as GpsService.LocalBinder
            gpsService = binder.getService()
            viewModel.serviceIsBound = true

            gpsService.activeRecordId.observe(viewLifecycleOwner, { id ->
                viewModel.activeRecordId = id
                viewModel.setLocationsByActiveRecordId()
            })
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            viewModel.serviceIsBound = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)
        dialog?.setTitle(getString(R.string.statistics_title))

        viewModel.allLocations.observe(viewLifecycleOwner, {
            viewModel.setLocationsByActiveRecordId()
            updateStatisticsDisplay()
        })

        return view
    }

    override fun onStart() {
        super.onStart()
        Intent(activity, GpsService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.unbindService(connection)
        viewModel.serviceIsBound = false
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
            RecordFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
    }
}