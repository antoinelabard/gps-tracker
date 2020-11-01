package fr.labard.simplegpstracker.ui.tracker

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
import fr.labard.simplegpstracker.model.tracker.FollowFragmentViewModel
import fr.labard.simplegpstracker.model.tracker.FollowFragmentViewModelFactory
import fr.labard.simplegpstracker.model.util.Constants
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

class FollowFragment : Fragment() {

    lateinit var mapView: MapView
    private lateinit var mapController: IMapController

    private val viewModel by viewModels<FollowFragmentViewModel> {
        FollowFragmentViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }


    private val locationBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent){
            if (intent.getStringExtra(Constants.Service.MODE) == Constants.Service.MODE_FOLLOW) {
                viewModel.currentLocation = Location("").apply {
                    latitude = intent.getDoubleExtra(Constants.Intent.LATITUDE_EXTRA, 0.0)
                    longitude = intent.getDoubleExtra(Constants.Intent.LONGITUDE_EXTRA, 0.0)
                }
                if (viewModel.locationsByRecordId.last().distanceTo(viewModel.currentLocation) < Constants.Location.MIN_RADIUS) {
                    viewModel.locationsByRecordId.removeLast()
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

        LocalBroadcastManager.getInstance(activity!!).registerReceiver(
            locationBroadcastReceiver,
            IntentFilter(Constants.Service.LOCATION_BROADCAST)
        )

        return view
    }

    private fun buildMapView() {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapController = mapView.controller
        mapController.setZoom(3.0)
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
        mapView.overlayManager.add(Polyline().apply { setPoints(parkour) })
        mapController.setCenter(parkour.last())
    }
}