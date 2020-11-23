package fr.labard.simplegpstracker.tracker

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import fr.labard.simplegpstracker.GPSApplication
import fr.labard.simplegpstracker.R
import fr.labard.simplegpstracker.data.local.LocationEntity
import fr.labard.simplegpstracker.util.Constants
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

    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController
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
                if (mode == Constants.Service.MODE_RECORD) {
                    activity_tracker_follow_fab.setImageResource(R.drawable.ic_baseline_pause_24)
                } else {
                    activity_tracker_follow_fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                }
            })

            gpsService.lastLocation.observe(viewLifecycleOwner, {location ->
                viewModel.currentLocation = location
            })
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            viewModel.serviceIsBound = false
        }
    }

    private lateinit var polyline: Polyline
    private lateinit var checkpoint: Marker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Configuration.getInstance().load(activity, activity?.getPreferences(Context.MODE_PRIVATE))
        val view = layoutInflater.inflate(R.layout.fragment_follow, container, false)
        mapView = view.findViewById(R.id.fragment_follow_mapview)

        buildMapView()

        viewModel.allLocations.observe(viewLifecycleOwner, object: Observer<List<LocationEntity>> {
            override fun onChanged(t: List<LocationEntity>?) {
                viewModel.locationsByRecordId = (viewModel.allLocations.value
                    ?.map { it.toLocation() }!! as MutableList<Location>)
                    .apply { sortBy { it.time } }


                updateMapView()
                viewModel.allLocations.removeObserver(this)
            }
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
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapController = mapView.controller
        mapController.setZoom(20.0)
        polyline = Polyline()
        checkpoint = Marker(mapView)
            val mLocationOverlay = MyLocationNewOverlay(
            GpsMyLocationProvider(activity?.applicationContext),
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
        mapView.overlays.remove(checkpoint)
        checkpoint = Marker(mapView).apply { icon = ContextCompat.getDrawable(
                activity?.applicationContext!!,
                R.drawable.ic_baseline_radio_button_unchecked_24
            )}
        mapView.overlayManager.add(polyline.apply { setPoints(parkour) })
        mapView.overlayManager.add(checkpoint.apply {
            position = GeoPoint(
                viewModel.currentLocation.latitude,
                viewModel.currentLocation.longitude
            )
            // particular behavior for the final checkpoint
            if (viewModel.locationsByRecordId.size == 1) {
                icon = ContextCompat.getDrawable(
                    activity?.applicationContext!!,
                    R.drawable.ic_baseline_radio_button_checked_24
                )
            }
        })
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