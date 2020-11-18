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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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

class MapFragment : Fragment() {

    private lateinit var localBroadcastManager: LocalBroadcastManager
    lateinit var locationServiceIntent: Intent
    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController

    private val viewModel by viewModels<MapFragmentViewModel> {
        MapFragmentViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }

    private val locationBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent){
            if (intent.getStringExtra(Constants.Service.MODE) == Constants.Service.MODE_RECORD) {
                val location = Location(Constants.Service.LOCATION_PROVIDER).apply {
                    latitude = intent.getDoubleExtra(Constants.Intent.LATITUDE_EXTRA, 0.0)
                    longitude = intent.getDoubleExtra(Constants.Intent.LONGITUDE_EXTRA, 0.0)
                    speed = intent.getFloatExtra(Constants.Intent.SPEED_EXTRA, 0.0f)
                    time = intent.getLongExtra(Constants.Intent.TIME_EXTRA, 0)
                }
                viewModel.insertLocation(location)
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

        viewModel.activeRecordId.observeForever{}

        viewModel.allLocations.observe(viewLifecycleOwner, {
            viewModel.setLocationsByActiveRecordId()
            updateMapView()
        })

        localBroadcastManager = LocalBroadcastManager.getInstance(activity!!.applicationContext)

        LocalBroadcastManager.getInstance(activity!!).registerReceiver(
            locationBroadcastReceiver,
            IntentFilter(Constants.Service.LOCATION_BROADCAST)
        )

        locationServiceIntent = Intent(activity?.applicationContext, GpsService::class.java)
            .putExtra(Constants.Intent.RECORD_ID_EXTRA, viewModel.activeRecordId.value)
            .putExtra(Constants.Intent.MODE, Constants.Service.MODE_RECORD)
            .setAction(Constants.Intent.ACTION_PLAY)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity_tracker_record_fab.setOnClickListener {
            if (viewModel.isRecording) {
                activity?.stopService(locationServiceIntent)
                activity_tracker_record_fab.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24)
            } else {
                activity?.startService(locationServiceIntent)
                activity_tracker_record_fab.setImageResource(R.drawable.ic_baseline_stop_24)
            }
            viewModel.isRecording = !viewModel.isRecording
        }
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

    override fun onDestroy() {
        super.onDestroy()
        activity?.stopService(locationServiceIntent)
        LocalBroadcastManager.getInstance(activity!!).unregisterReceiver(locationBroadcastReceiver)
    }
}