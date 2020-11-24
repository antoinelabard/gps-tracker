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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import fr.labard.gpsgpx.GPSApplication
import fr.labard.gpsgpx.R
import fr.labard.gpsgpx.util.Constants
import kotlinx.android.synthetic.main.fragment_follow.*
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

/**
 * FollowFragment displays the UI to follow a path previously recorded and stored or imported in the database.
 */
class FollowFragment : Fragment() {

    lateinit var gpsProvider: GpsMyLocationProvider
    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController

    private lateinit var polyline: Polyline
    private lateinit var checkpoint: Marker

    private val viewModel by viewModels<FollowFragmentViewModel> {
        FollowFragmentViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }
    private lateinit var gpsService: GpsService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as GpsService.LocalBinder
            gpsService = binder.getService()
            viewModel.serviceIsBound = true

            gpsService.gpsMode.observe(viewLifecycleOwner, {mode ->
                if (mode == Constants.Service.MODE_FOLLOW) {
                    activity_tracker_follow_fab.setImageResource(R.drawable.ic_baseline_pause_24)
                } else {
                    activity_tracker_follow_fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
            })
            gpsService.activeRecordId.observe(viewLifecycleOwner, { id ->
                viewModel.activeRecordId = id
                viewModel.setLocationsByActiveRecordId()
                updateMapView()
            })
            gpsService.lastLocation.observe(viewLifecycleOwner, {location ->
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
    ): View? {
        Configuration.getInstance().load(activity, activity?.getPreferences(Context.MODE_PRIVATE))
        val view = layoutInflater.inflate(R.layout.fragment_follow, container, false)
        mapView = view.findViewById(R.id.fragment_follow_mapview)

        buildMapView()

        viewModel.allLocations.observe(viewLifecycleOwner, {
            viewModel.setLocationsByActiveRecordId()
            updateMapView()
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity_tracker_follow_fab.setOnClickListener {
            if (gpsService.gpsMode.value == Constants.Service.MODE_FOLLOW) {
                gpsService.gpsMode.value = Constants.Service.MODE_STANDBY
            } else
                gpsService.gpsMode.value = Constants.Service.MODE_FOLLOW
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
        checkpoint = Marker(mapView)
            val mLocationOverlay = MyLocationNewOverlay(
            gpsProvider,
            mapView
        )
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
        mapView.overlays.remove(polyline)
        polyline = Polyline()
        mapView.overlayManager.add(polyline.apply { setPoints(parkour) })

        mapView.overlays.remove(checkpoint)
        checkpoint = Marker(mapView).apply {
            icon = ContextCompat.getDrawable(
                activity?.applicationContext!!,
                if (viewModel.locationsByRecordId.size == 1)
                    R.drawable.ic_baseline_radio_button_checked_24
                else R.drawable.ic_baseline_radio_button_unchecked_24
            )
            position = GeoPoint(
                parkour.last().latitude,
                parkour.last().longitude
            )
        }
        mapView.overlayManager.add(checkpoint)
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
        super.onStop()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
}