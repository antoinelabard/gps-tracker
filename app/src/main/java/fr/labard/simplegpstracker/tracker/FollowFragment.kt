package fr.labard.simplegpstracker.tracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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

class FollowFragment : Fragment() {

    lateinit var mapView: MapView
    private lateinit var mapController: IMapController
    lateinit var locationServiceIntent: Intent

    lateinit var polyline: Polyline
    lateinit var checkpoint: Marker

    private val viewModel by viewModels<FollowFragmentViewModel> {
        FollowFragmentViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }


    private val locationBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent){
            if (intent.getStringExtra(Constants.Service.MODE) == Constants.Service.MODE_FOLLOW
                && viewModel.locationsByRecordId.isNotEmpty()) {
                viewModel.currentLocation = Location("").apply {
                    latitude = intent.getDoubleExtra(Constants.Intent.LATITUDE_EXTRA, 0.0)
                    longitude = intent.getDoubleExtra(Constants.Intent.LONGITUDE_EXTRA, 0.0)
                }
                if (viewModel.locationsByRecordId.last().distanceTo(viewModel.currentLocation) < Constants.Location.MIN_RADIUS) {
                    viewModel.locationsByRecordId.removeLast()
                    Toast.makeText(activity, getString(R.string.checkpoint_reached), Toast.LENGTH_SHORT).show()
                }
                updateMapView()
            }
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

        viewModel.allLocations.observe(viewLifecycleOwner, object: Observer<List<LocationEntity>> {
            override fun onChanged(t: List<LocationEntity>?) {
                viewModel.locationsByRecordId = (viewModel.allLocations.value
                    ?.map { it.toLocation() }!! as MutableList<Location>)
                    .apply { sortBy { it.time } }


                updateMapView()
                viewModel.allLocations.removeObserver(this)
            }
        })

        LocalBroadcastManager.getInstance(activity!!).registerReceiver(
            locationBroadcastReceiver,
            IntentFilter(Constants.Service.LOCATION_BROADCAST)
        )

        locationServiceIntent = Intent(activity?.applicationContext, GpsService::class.java)
            .putExtra(Constants.Intent.RECORD_ID_EXTRA, viewModel.activeRecordId.value)
            .putExtra(Constants.Intent.MODE, Constants.Service.MODE_FOLLOW)
            .setAction(Constants.Intent.ACTION_PLAY)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity_tracker_record_fab.setOnClickListener {
            if (viewModel.isRecording) {
                activity?.stopService(locationServiceIntent)
                activity_tracker_record_fab.setImageResource(R.drawable.ic_action_gps_active)
            } else {
                activity?.startService(locationServiceIntent)
                activity_tracker_record_fab.setImageResource(R.drawable.ic_action_gps_inactive)
            }
            viewModel.isRecording = !viewModel.isRecording
        }
    }

    private fun buildMapView() {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapController = mapView.controller
        mapController.setZoom(10.0)
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
        checkpoint = Marker(mapView)
            .apply { icon = ContextCompat.getDrawable(activity?.applicationContext!!, R.drawable.ic_add_black_24dp) }
        mapView.overlayManager.add(polyline.apply { setPoints(parkour) })
        mapView.overlayManager.add(checkpoint.apply {
            position = GeoPoint(
                viewModel.locationsByRecordId.last().latitude,
                viewModel.locationsByRecordId.last().longitude
            )
            if (viewModel.locationsByRecordId.size == 1) {
                icon = ContextCompat.getDrawable(activity?.applicationContext!!, R.drawable.ic_action_delete)
            }
        })
        mapController.setCenter(GeoPoint(
            viewModel.currentLocation.latitude,
            viewModel.currentLocation.longitude,
        ))
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.stopService(locationServiceIntent)
    }
}