package fr.labard.simplegpstracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    lateinit var mapView: MapView
    private lateinit var mapController: IMapController

    private val viewModel by viewModels<MapFragmentViewModel> {
        MapFragmentViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        setFragmentResult(Constants.Intent.REQUEST_RECORD_ID, bundleOf(
//            Constants.Intent.RECORD_ID_EXTRA to arguments?.getString(Constants.Intent.RECORD_ID_EXTRA)
//        ))

//        viewModel.recordId = arguments?.getString(Constants.Intent.RECORD_ID_EXTRA)!!

        val view = layoutInflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.fragment_map_mapview)
        buildMapView()

//        setFragmentResultListener(Constants.Intent.REQUEST_RECORD_ID) { _, bundle ->
//            viewModel.recordId = bundle.getString(Constants.Intent.RECORD_ID_EXTRA)!!
//        }

        viewModel.allLocations.observe(viewLifecycleOwner, {
            viewModel.locationsByRecordId = viewModel.allLocations.value?.filter {
                it.recordId == viewModel.activeRecordId.value
            }!!
            updateMapView()
        })

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun buildMapView() {
        /*requestPermissionsIfNecessary(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        ))*/

        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapController = mapView.controller
        mapController.setZoom(3.0)
        val mLocationOverlay = MyLocationNewOverlay(
            GpsMyLocationProvider(activity),
            mapView
        )
        mLocationOverlay.enableMyLocation()
        mapView.overlays.add(mLocationOverlay)

        val mCompassOverlay = CompassOverlay(
                activity,
            InternalCompassOrientationProvider(activity),
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

    /*override fun onDestroy() {
        super.onDestroy()
        stopService(locationServiceIntent)
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(LocationBroadcastReceiver)
    }*/
}