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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import fr.labard.gpsgpx.GPSApplication
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.databinding.FragmentRecordBinding
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
 * RecordFragment displays the UI to show the path recorded in real time.
 */
class RecordFragment : Fragment() {

    private lateinit var gpsProvider: GpsMyLocationProvider
    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController

    private lateinit var polyline: Polyline

    private val viewModel by viewModels<RecordFragmentViewModel> {
        MapFragmentViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }
    private  var gpsService: GpsService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as GpsService.LocalBinder
            gpsService = binder.getService()
            viewModel.serviceIsBound = true

//            gpsService.gpsMode.observe(viewLifecycleOwner, {mode ->
//                if (mode == Constants.Service.MODE_RECORD) {
//                    fragment_record_fab.setImageResource(R.drawable.ic_baseline_stop_24)
//                } else {
//                    fragment_record_fab.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24)
//                }
//            })
            gpsService?.activeRecordId?.observe(viewLifecycleOwner, { id ->
                viewModel.activeRecordId = id
                viewModel.setLocationsByActiveRecordId()
                updateMapView()
            })
            gpsService?.lastLocation?.observe(viewLifecycleOwner, { location ->
                viewModel.currentLocation = location
            })
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            viewModel.serviceIsBound = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentRecordBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        val view = binding.root

        Configuration.getInstance().load(activity, activity?.getPreferences(Context.MODE_PRIVATE))
        mapView = view.findViewById(R.id.fragment_record_mapview)

        buildMapView()

        viewModel.allLocations.observe(viewLifecycleOwner, {
            viewModel.setLocationsByActiveRecordId()
            updateMapView()
        })

        viewModel.mode.observe(viewLifecycleOwner, { mode ->
            if (gpsService == null) {
                gpsService?.gpsMode?.value = mode
            }
        })

        return view
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        fragment_record_fab.setOnClickListener {
//            if (gpsService.gpsMode.value == Constants.Service.MODE_RECORD) {
//                gpsService.gpsMode.value = Constants.Service.MODE_STANDBY
//            } else {
//                gpsService.gpsMode.value = Constants.Service.MODE_RECORD
//            }
//        }
//    }

    override fun onStart() {
        Intent(activity, GpsService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        activity?.unbindService(connection)
        viewModel.serviceIsBound = false
    }

    private fun buildMapView() {
        gpsProvider = GpsMyLocationProvider(activity?.applicationContext)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapController = mapView.controller.apply {
            setZoom(20.0)
            gpsProvider.lastKnownLocation?.let {
                setCenter(
                    GeoPoint(
                        gpsProvider.lastKnownLocation.latitude,
                        gpsProvider.lastKnownLocation.longitude

                    )
                )
            }
        }
        polyline = Polyline()
        val mLocationOverlay = MyLocationNewOverlay(
            gpsProvider,
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
        val parkour: List<GeoPoint> = viewModel.locationsByRecordId.map { GeoPoint(it.latitude, it.longitude) }
        if (parkour.isEmpty()) return
        mapView.overlays.remove(polyline)
        polyline = Polyline()
        mapView.overlayManager.add(polyline.apply { setPoints(parkour) })
        mapController.apply {
            gpsProvider.lastKnownLocation?.let {
                setCenter(
                    GeoPoint(
                        gpsProvider.lastKnownLocation.latitude,
                        gpsProvider.lastKnownLocation.longitude

                    )
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
}