package fr.labard.simplegpstracker.tracker

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import fr.labard.simplegpstracker.GPSApplication
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.util.Constants
import kotlinx.android.synthetic.main.fragment_map.*
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

/**
 * MapFragment displays the UI to show the path recorded in real time.
 */
class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController
    private val viewModel by viewModels<MapFragmentViewModel> {
        MapFragmentViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }
    private lateinit var gpsService: GpsService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as GpsService.LocalBinder
            gpsService = binder.getService()
            viewModel.serviceIsBound = true

            viewModel.activeRecordId = gpsService.activeRecordId
            gpsService.gpsMode.observe(viewLifecycleOwner, {mode ->
                if (mode == Constants.Service.MODE_RECORD) {
                    activity_tracker_record_fab.setImageResource(R.drawable.ic_baseline_stop_24)
                } else {
                    activity_tracker_record_fab.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24)
                }
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

        Configuration.getInstance().load(activity, activity?.getPreferences(Context.MODE_PRIVATE))
        val view = layoutInflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.fragment_map_mapview)
        buildMapView()

        viewModel.allLocations.observe(viewLifecycleOwner, {
            viewModel.setLocationsByActiveRecordId()
            updateMapView()
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity_tracker_record_fab.setOnClickListener {
            if (gpsService.gpsMode.value == Constants.Service.MODE_RECORD) {
                gpsService.gpsMode.value = Constants.Service.MODE_STANDBY
            } else {
                gpsService.gpsMode.value = Constants.Service.MODE_RECORD
            }
        }
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

    private fun buildMapView() {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapController = mapView.controller
        mapController.setZoom(20.0)
        val mLocationOverlay = MyLocationNewOverlay(
            GpsMyLocationProvider(activity?.applicationContext),
            mapView)
        mLocationOverlay.enableMyLocation()
        mapView.overlays.add(mLocationOverlay)

        val mCompassOverlay = CompassOverlay(
            activity,
            InternalCompassOrientationProvider(activity?.applicationContext),
            mapView
        )

        mCompassOverlay.enableCompass()

        mapView.overlays.apply {
            add(mLocationOverlay)
            add(mCompassOverlay)
        }
    }

    private fun updateMapView() {
        val parkour: List<GeoPoint>? = viewModel.locationsByRecordId.map { GeoPoint(it.latitude, it.longitude) }
        if (parkour!!.isEmpty()) return
        mapView.overlayManager.add(Polyline().apply { setPoints(parkour) })
        mapController.setCenter(parkour.last())
    }

    override fun onPause() {
        super.onStop()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
}